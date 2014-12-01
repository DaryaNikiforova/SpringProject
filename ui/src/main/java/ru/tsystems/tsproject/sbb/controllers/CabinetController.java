package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.tsproject.sbb.exceptions.BadRequestException;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.exceptions.TicketNotFoundException;
import ru.tsystems.tsproject.sbb.services.exceptions.UserNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by apple on 28.11.14.
 */
@Controller
@RequestMapping(value = "cabinet")
public class CabinetController {

    @Inject TicketService ticketService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getTickets(Model model) throws PageNotFoundException {
        UserDetails userDetails = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        if (userDetails != null) {
            try {
                List<TicketTO> tickets = ticketService.getTickets(userDetails.getUsername());
                model.addAttribute("tickets", tickets);
            } catch (UserNotFoundException e) {
                throw new PageNotFoundException(1);
            }
            return "ticket/getTickets";
        } else {
            throw new PageNotFoundException(1);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public String viewTicket(@PathVariable("id") String id, Model model) throws PageNotFoundException, BadRequestException {
        UserDetails userDetails = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }

        if (userDetails != null) {
            TicketTO ticket = null;
            try {
                ticket = ticketService.getTicket(Integer.parseInt(id), userDetails.getUsername());
            } catch (TicketNotFoundException e) {
                throw new PageNotFoundException(1);
            } catch (UserNotFoundException e) {
                throw new PageNotFoundException(1);
            } catch (NumberFormatException e) {
                throw new BadRequestException();
            }
            model.addAttribute("ticket", ticket);
            return "ticket/ticketInfo";
        } else {
            throw new PageNotFoundException(1);
        }
    }
}
