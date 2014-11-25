package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
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
        try {
            model.addAttribute("stations", stationService.getStations());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return ADDROUTE;
    }

    @RequestMapping(method = RequestMethod.POST, value = "add")
    public String postAddRoute(Model model, @ModelAttribute("route") @Valid RouteTO routeTO, BindingResult result) {
        try {
            model.addAttribute("stations", stationService.getStations());
            if (!result.hasErrors()) {
                routeService.addRoute(routeTO);
                return "redirect:/main/route";
            } else {
                model.addAttribute("errors", result.getAllErrors());
            }
        } catch (StationNotFoundException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return ADDROUTE;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getRoutes(Model model) {
        model.addAttribute("routes", routeService.getAllRoutes());
        return "route/getRoutes";
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteRoute(Model model, @PathVariable("id") Integer id) {
        List<TripTO> trips = tripService.getTripsByRoute(id);
        if (!trips.isEmpty()) {
            List<String> errors = new ArrayList<String>();
            StringBuilder builder = new StringBuilder("По данному маршруту направляются следующие рейсы: ");
            for (int i = 0; i < trips.size(); i++) {
                builder.append(trips.get(i).getId());
                if (i != (trips.size() - 1)) {
                    builder.append(", ");
                }
            }
            builder.append(". Сначала отредактируйте рейсы");
            errors.add(builder.toString());
            model.addAttribute("errors", errors);
            model.addAttribute("routes", routeService.getAllRoutes());
            return "route/getRoutes";
        } else {
            RouteTO route = new RouteTO();
            route.setNumber(id);
            try {
                routeService.deleteRoute(route);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/main/route/getRoutes";
    }

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public String editRoute(Model model, @PathVariable("id") Integer id) {
        try {
            RouteTO routeTO = routeService.getRoute(id);
            model.addAttribute("route", routeTO);
            model.addAttribute("stations", stationService.getStations());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return EDITROUTE;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public String postEditRoute(Model model, @ModelAttribute("route") @Valid RouteTO routeTO, BindingResult result) {
        try {
            model.addAttribute("stations", stationService.getStations());
            if (!result.hasErrors()) {
                routeService.editRoute(routeTO);
                return "redirect:/main/route";
            } else {
                model.addAttribute("errors", result.getAllErrors());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EDITROUTE;
    }
}
