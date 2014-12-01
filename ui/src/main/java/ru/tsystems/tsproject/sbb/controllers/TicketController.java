package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.tsproject.sbb.exceptions.BadRequestException;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.exceptions.*;
import ru.tsystems.tsproject.sbb.transferObjects.ServiceTO;
import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 17.11.14.
 */
@Controller
@RequestMapping("/ticket")
@SessionAttributes("ticket")
public class TicketController {

    @Inject private TicketService ticketService;

    @RequestMapping(method = RequestMethod.GET, value = "buy")
    public String buyTicket(@RequestParam("tripId") String tripId,
                            @RequestParam("stationFrom") String stationFrom,
                            @RequestParam("stationTo") String stationTo, Model model, SessionStatus status)
            throws PageNotFoundException, BadRequestException, TimeConstraintException, UserAlreadyRegisteredException, NoAvailableSeats {
        if ((tripId != null && stationFrom != null && stationTo != null) && (!tripId.isEmpty()
                && !stationFrom.isEmpty() && !stationTo.isEmpty())) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = null;
            if (principal instanceof UserDetails) {
                userDetails = (UserDetails) principal;
            }
            //if (model.containsAttribute("ticket")) {
              //  status.setComplete();
            //}
            if (userDetails != null) {
                int id = 0;
                try {
                    id = Integer.parseInt(tripId);
                    model.addAttribute("ticket", ticketService.generateTicket(id, stationFrom, stationTo, userDetails.getUsername()));
                } catch (TripNotFoundException e) {
                    throw new PageNotFoundException(id);
                } catch (TripDetailsNotFoundException e) {
                    throw new PageNotFoundException(id);
                } catch (UserNotFoundException e) {
                    throw new PageNotFoundException(id);
                } catch(NumberFormatException e) {
                    throw new BadRequestException();
                }
                return "ticket/buyTicket";
            }

            return "search/searchtrip";
        }

        return "search/searchtrip";
    }

    @RequestMapping(method = RequestMethod.POST, value = "buy")
    public String postBuyTicket(@ModelAttribute("ticket") TicketTO ticket) throws PageNotFoundException {
        List<ServiceTO> serviceTOs = ticketService.completeServices(ticket.getServices());
//        for(Integer i : services) {
//            for(ServiceTO s : serviceTOs) {
//                if(s.getId() != i)
//                    ticket.getServices().remove(s);
//            }
//        }



        Map<Long, String> rateTypes = ticket.getRateTypes();
        List<Integer> seats = ticket.getSeats();
        boolean first = rateTypes.containsKey((long) ticket.getRateType());
        boolean second = seats.contains(ticket.getSeatNumber());
        if (first && second) {
            ticket.setRateName(rateTypes.get(new Long(ticket.getRateType())));
            try {
                ticket.setPrice(ticketService.calcPrice(ticket));
            } catch (TripDetailsNotFoundException e) {
                throw new PageNotFoundException(ticket.getTripId());
            }
        }
        return "ticket/purchasesummary";
    }

    @RequestMapping(method = RequestMethod.POST, value = "purchase")
    public String purchaseTicket(@ModelAttribute("ticket") TicketTO ticket, Model model, SessionStatus status)
            throws UserAlreadyRegisteredException, SeatAlreadyRegisteredException, TimeConstraintException {
        ticketService.AddTicket(ticket);
        model.addAttribute("ticket", ticket);
        model.addAttribute("isSuccess", true);
        status.setComplete();
        return "ticket/purchasesummary";
    }

    @ExceptionHandler(SeatAlreadyRegisteredException.class)
    public ModelAndView handleSeatAlreadyRegistered(HttpServletRequest req, Exception exception) {
        //logger.error("Request: " + req.getRequestURL() + " raised " + exception);
        ModelAndView mav = new ModelAndView();
        mav.addObject("headerText", "Извините!");
        mav.addObject("message", "Это место уже занято");
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ModelAndView handleUserAlreadyRegistered(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("headerText", "Покупка уже совершена!");
        mav.addObject("message", "Не беспокойтесь, Вы уже зарегистрированы на этот рейс.");
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(TimeConstraintException.class)
    public ModelAndView handleTimeConstraint(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("headerText", "Покупка недоступна!");
        mav.addObject("message", "Продажа билетов на данный рейс уже закрыта.");
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(NoAvailableSeats.class)
    public ModelAndView availableSeatsConstraint(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("headerText", "Покупка недоступна!");
        mav.addObject("message", "К сожалению, на этот рейс все билеты уже проданы.");
        mav.setViewName("error");
        return mav;
    }
}
