package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
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
                            @RequestParam("stationTo") String stationTo, Model model) {
        if ((tripId != null && stationFrom != null && stationTo != null) && (!tripId.isEmpty() && !stationFrom.isEmpty() && !stationTo.isEmpty())) {
            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                UserDetails userDetails = null;
                if (principal instanceof UserDetails) {
                    userDetails = (UserDetails) principal;
                }
                model.addAttribute("ticket", ticketService.generateTicket(Integer.parseInt(tripId), stationFrom, stationTo, userDetails.getUsername()));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return "ticket/buyTicket";
        }

        return "search/searchtrip";
    }

    @RequestMapping(method = RequestMethod.POST, value = "buy")
    public String postBuyTicket(HttpServletRequest request, @ModelAttribute("ticket") TicketTO ticket) {
        Map<Long, String> rateTypes = ticket.getRateTypes();
        List<Integer> seats = ticket.getSeats();
        boolean first = rateTypes.containsKey((long) ticket.getRateType());
        boolean second = seats.contains(ticket.getSeatNumber());
        if (first && second) {
            ticket.setRateName(rateTypes.get(new Long(ticket.getRateType())));
            try {
                ticket.setPrice(ticketService.calcPrice(ticket));
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        return "ticket/purchasesummary";
    }

    @RequestMapping(method = RequestMethod.POST, value = "purchase")
    public String purchaseTicket(@ModelAttribute("ticket") TicketTO ticket, Model model, SessionStatus status) {
        try {
            ticketService.AddTicket(ticket);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        model.addAttribute("ticket", ticket);
        model.addAttribute("isSuccess", true);
        status.setComplete();
        return "ticket/purchasesummary";
    }

}
