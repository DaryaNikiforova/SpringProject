package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17.11.14.
 */
@Controller
@RequestMapping("/station")
public class StationController {
    @Inject private StationService stationService;
    @Inject private RouteService routeService;

    private static final String ADDSTATION = "station/addStation";
    private static final String EDITSTATION = "station/editStation";
    private static final String GETSTATIONS = "station/getStations";

    @RequestMapping(method = RequestMethod.GET, value = "add")
    public String addStation(Model model) {
        model.addAttribute("station", new StationTO());
        return ADDSTATION;
    }

    @RequestMapping(method = RequestMethod.POST, value = "add")
    public String postAddStation(Model model, @ModelAttribute("station") @Valid StationTO stationTO, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                stationService.addStation(stationTO);
            } catch (StationAlreadyExistException e) {
                result.rejectValue("name", "error.station", "Такая станция уже существует в базе данных");
                model.addAttribute("errors", result.getAllErrors());
                return ADDSTATION;
            }
            return "redirect:getStations";
        }
        else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return ADDSTATION;
    }

    @RequestMapping(method = RequestMethod.GET, value = "getStations")
    public String getStations(Model model) {
        try {
            model.addAttribute("stations", stationService.getStations());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return GETSTATIONS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public String editStation(Model model, @PathVariable("id") Integer id) throws PageNotFoundException {
        try {
            StationTO station = stationService.getStation(id);
            model.addAttribute("station", station);
        } catch (StationNotFoundException e) {
            throw new PageNotFoundException(id);
        }
        return EDITSTATION;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public String postEditStation( Model model, @ModelAttribute("station") @Valid StationTO station, BindingResult result) throws PageNotFoundException {
        if (!result.hasErrors()) {
            try {
                stationService.editStation(station);
            } catch (StationAlreadyExistException e) {
                result.rejectValue("name", "error.station", "Такая станция уже существует в базе данных");
                model.addAttribute("errors", result.getAllErrors());
                return EDITSTATION;
            }
            return "redirect:/main/station/getStations";
        } else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return EDITSTATION;
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteStation(@PathVariable("id") Integer id, Model model) throws PageNotFoundException {
        try {
            List<RouteTO> routes = routeService.getRoutesByStation(id);
            if (!routes.isEmpty()) {
                List<String> errors = new ArrayList<String>();
                StringBuilder builder = new StringBuilder("Станция содержится в маршрутах: ");
                for (int i = 0; i < routes.size(); i++) {
                    builder.append(routes.get(i).getNumber());
                    if (i != (routes.size() - 1)) {
                        builder.append(", ");
                    }
                }
                builder.append(". Сначала отредактируйте маршруты");
                errors.add(builder.toString());
                model.addAttribute("errors", errors);
                model.addAttribute("stations", stationService.getStations());
                return GETSTATIONS;
            } else {
                try {
                    StationTO station = stationService.getStation(id);
                    stationService.deleteStation(station);
                } catch (StationNotFoundException e) {
                    throw new PageNotFoundException(id);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/main/" + GETSTATIONS;
    }
}
