package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.transferObjects.PassengerTO;
import ru.tsystems.tsproject.sbb.transferObjects.TripTO;

import javax.inject.Inject;
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
        try {
            model.addAttribute("trips", tripService.getAllTrips());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "trip/getTrips";
    }

    @RequestMapping(method = RequestMethod.GET, value = "passengers")
    public String getPassengers(Model model, @RequestParam("tripId") String id) {
        int tripId = Integer.parseInt(id);
        List<PassengerTO> passengerTOs = ticketService.getPassengersByTrip(tripId);
        model.addAttribute("passengers", passengerTOs);
        return "trip/getPassengers";
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteTrip(@PathVariable("id") Integer id) {
        TripTO trip = new TripTO();
        trip.setId(id);
        try {
            tripService.deleteTrip(trip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/main/trip";
    }
}
