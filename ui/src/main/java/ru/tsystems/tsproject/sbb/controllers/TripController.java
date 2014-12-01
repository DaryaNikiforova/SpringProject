package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.tsproject.sbb.exceptions.BadRequestException;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.responses.ErrorMessage;
import ru.tsystems.tsproject.sbb.responses.ValidationResponse;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.TripNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.PassengerTO;
import ru.tsystems.tsproject.sbb.transferObjects.TripTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17.11.14.
 */

@Controller
@RequestMapping("/trip")
public class TripController {

    @Inject private TripService tripService;
    @Inject private TicketService ticketService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getTrips(Model model) {
        model.addAttribute("trips", tripService.getAllTrips());
        return "trip/getTrips";
    }

    @RequestMapping(method = RequestMethod.GET, value = "passengers")
    public String getPassengers(Model model, @RequestParam("tripId") String tripId) throws PageNotFoundException, BadRequestException {
        int id = 0;
        List<PassengerTO> passengerTOs = null;
        try {
            id = Integer.parseInt(tripId);
            passengerTOs = ticketService.getPassengersByTrip(id);
        } catch (TripNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        model.addAttribute("passengers", passengerTOs);
        return "trip/getPassengers";
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteTrip(@PathVariable("id") String tripId) throws PageNotFoundException, BadRequestException {
        TripTO trip = new TripTO();
        int id = 0;
        try {
            id = Integer.parseInt(tripId);
            trip.setId(id);
            tripService.deleteTrip(trip);
        } catch (TripNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        return "redirect:/main/trip";
    }

    @RequestMapping(value = "delete/{id}.json")
    public @ResponseBody ValidationResponse deleteTripAjax(@PathVariable("id") String tripId)
            throws PageNotFoundException, BadRequestException {
        ValidationResponse response = new ValidationResponse();
        List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
        int id = 0;
        try {
            id = Integer.parseInt(tripId);
            TripTO trip = new TripTO();
            trip.setId(id);
            if (!tripService.isTripHasTickets(trip)) {
                tripService.deleteTrip(trip);
                response.setStatus("SUCCESS");
            } else {
                errorMessages.add(new ErrorMessage("all", getDeleteErrorMessage()));
                response.setStatus("FAIL");
            }
        } catch (TripNotFoundException e) {
            errorMessages.add(new ErrorMessage("all", "Ошибка при удалении"));
            response.setStatus("FAIL");
        } catch (NumberFormatException e) {
            errorMessages.add(new ErrorMessage("all", "Ошибка при удалении"));
            response.setStatus("FAIL");
        }
        response.setErrorMessageList(errorMessages);
        return response;
    }

    private String getDeleteErrorMessage() {
        return "На этот рейс куплены билеты. Отмените покупки.";
    }
}
