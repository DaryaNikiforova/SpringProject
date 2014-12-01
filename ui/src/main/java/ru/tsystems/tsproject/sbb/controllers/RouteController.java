package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.tsproject.sbb.exceptions.BadRequestException;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.responses.ErrorMessage;
import ru.tsystems.tsproject.sbb.responses.ValidationResponse;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.RouteNotFoundException;
import ru.tsystems.tsproject.sbb.services.exceptions.TripNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;
import ru.tsystems.tsproject.sbb.transferObjects.TripTO;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17.11.14.
 */
@Controller
@RequestMapping("/route")
public class RouteController {

    @Inject private StationService stationService;
    @Inject private RouteService routeService;
    @Inject private TripService tripService;

    private static final String ADDROUTE = "route/addRoute";
    private static final String EDITROUTE = "route/editRoute";

    @ModelAttribute("route")
    public RouteTO getRouteTO() {
        RouteTO routeTO = new RouteTO();
        routeTO.setRouteEntries(new AutoPopulatingList<RouteEntryTO>(RouteEntryTO.class));
        return routeTO;
    }

    @RequestMapping(method = RequestMethod.GET, value = "add")
    public String addRoute(Model model) {
        model.addAttribute("stations", stationService.getStations());
        return ADDROUTE;
    }

    @RequestMapping(method = RequestMethod.POST, value = "add")
    public String postAddRoute(Model model, @ModelAttribute("route") @Valid RouteTO routeTO, BindingResult result) {
        model.addAttribute("stations", stationService.getStations());
        if (!result.hasErrors()) {
            routeService.addRoute(routeTO);
            return "redirect:/main/route";
        } else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return ADDROUTE;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getRoutes(Model model) {
        model.addAttribute("routes", routeService.getAllRoutes());
        return "route/getRoutes";
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteRoute(Model model, @PathVariable("id") String stationId) throws PageNotFoundException, BadRequestException {
        List<TripTO> trips;
        int id = 0;
        try {
            id = Integer.parseInt(stationId);
            trips = tripService.getTripsByRoute(id);
        } catch (TripNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        if (!trips.isEmpty()) {
            List<String> errors = new ArrayList<String>();
            errors.add(getDeleteErrorMessage(trips));
            model.addAttribute("errors", errors);
            model.addAttribute("routes", routeService.getAllRoutes());
            return "route/getRoutes";
        } else {
            RouteTO route = new RouteTO();
            route.setNumber(id);
            try {
                routeService.deleteRoute(route);
            } catch (RouteNotFoundException e) {
                throw new PageNotFoundException(id);
            }
        }
        return "redirect:/main/route/";
    }

    @RequestMapping(value = "delete/{id}.json")
    public @ResponseBody ValidationResponse deleteRouteAjax(@PathVariable("id") String stationId)
            throws PageNotFoundException, BadRequestException {
        ValidationResponse response = new ValidationResponse();
        List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
        List<TripTO> trips;
        int id = 0;
        try {
            id = Integer.parseInt(stationId);
            trips = tripService.getTripsByRoute(id);
        } catch (TripNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        if (!trips.isEmpty()) {
            errorMessages.add(new ErrorMessage("all", getDeleteErrorMessage(trips)));
            response.setStatus("FAIL");
        } else {
            RouteTO route = new RouteTO();
            route.setNumber(id);
            try {
                routeService.deleteRoute(route);
                response.setStatus("SUCCESS");
            } catch (RouteNotFoundException e) {
                errorMessages.add(new ErrorMessage("all", "Ошибка при удалении"));
                response.setStatus("FAIL");
            }
        }
        response.setErrorMessageList(errorMessages);
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public String editRoute(Model model, @PathVariable("id") String routeId) throws PageNotFoundException, BadRequestException {
        RouteTO routeTO;
        int id = 0;
        try {
            id = Integer.parseInt(routeId);
            routeTO = routeService.getRoute(id);
        } catch (RouteNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        model.addAttribute("route", routeTO);
        model.addAttribute("stations", stationService.getStations());
        return EDITROUTE;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public String postEditRoute(Model model, @ModelAttribute("route") @Valid RouteTO routeTO, BindingResult result) throws PageNotFoundException {
        model.addAttribute("stations", stationService.getStations());
        if (!result.hasErrors()) {
            try {
                routeService.editRoute(routeTO);
            } catch (RouteNotFoundException e) {
                throw new PageNotFoundException(routeTO.getNumber());
            }

            return "redirect:/main/route";
        } else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return EDITROUTE;
    }

    private String getDeleteErrorMessage(List<TripTO> trips) {
        StringBuilder builder = new StringBuilder("По данному маршруту направляются следующие рейсы: ");
        for (int i = 0; i < trips.size(); i++) {
            builder.append(trips.get(i).getId());
            if (i != (trips.size() - 1)) {
                builder.append(", ");
            }
        }
        builder.append(". Сначала отредактируйте рейсы");
        return builder.toString();
    }
}
