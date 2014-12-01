package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.tsproject.sbb.exceptions.BadRequestException;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.responses.ErrorMessage;
import ru.tsystems.tsproject.sbb.responses.ValidationResponse;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.services.StationService;
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
            return "redirect:/main/station";
        }
        else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return ADDSTATION;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getStations(Model model) {
        model.addAttribute("stations", stationService.getStations());
        return GETSTATIONS;
    }

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public String editStation(Model model, @PathVariable("id") String stationId) throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(stationId);
            StationTO station = stationService.getStation(id);
            model.addAttribute("station", station);
        } catch (StationNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        return EDITSTATION;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public String postEditStation( Model model, @ModelAttribute("station") @Valid StationTO station, BindingResult result) throws PageNotFoundException {
        if (!result.hasErrors()) {
            try {
                stationService.editStation(station);
            //} catch (StationAlreadyExistException e) {
              //  result.rejectValue("name", "error.station", "Такая станция уже существует в базе данных");
              //  model.addAttribute("errors", result.getAllErrors());
              //  return EDITSTATION;
            } catch (StationNotFoundException e) {
                throw new PageNotFoundException(station.getId());
            }
            return "redirect:/main/station/";
        } else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return EDITSTATION;
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteStation(@PathVariable("id") String stationId, Model model) throws PageNotFoundException {
        int id = 0;
        try {
            id = Integer.parseInt(stationId);
            List<RouteTO> routes = routeService.getRoutesByStation(id);
            if (!routes.isEmpty()) {
                List<String> errors = new ArrayList<String>();
                errors.add(getDeleteErrorMessage(routes));
                model.addAttribute("errors", errors);
                model.addAttribute("stations", stationService.getStations());
                return GETSTATIONS;
            } else {
                StationTO station = stationService.getStation(id);
                stationService.deleteStation(station);
            }
        } catch (StationNotFoundException e) {
            throw new PageNotFoundException(id);
        }
        return "redirect:/main/station/";
    }

    @RequestMapping(value = "delete/{id}.json")
    public @ResponseBody ValidationResponse deleteStationAjax(@PathVariable("id") String stationId) throws PageNotFoundException {
        ValidationResponse response = new ValidationResponse();
        List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
        int id = 0;
        try {
            id = Integer.parseInt(stationId);
            List<RouteTO> routes = routeService.getRoutesByStation(id);
            if (!routes.isEmpty()) {
                errorMessages.add(new ErrorMessage("all", getDeleteErrorMessage(routes)));
                response.setStatus("FAIL");
            } else {
                StationTO station = stationService.getStation(id);
                stationService.deleteStation(station);
                response.setStatus("SUCCESS");

            }
        } catch (StationNotFoundException e) {
            errorMessages.add(new ErrorMessage("all", "Ошибка при удалении"));
            response.setStatus("FAIL");
        }
        response.setErrorMessageList(errorMessages);
        return response;
    }

    private String getDeleteErrorMessage(List<RouteTO> routes) {
        StringBuilder builder = new StringBuilder("Станция содержится в маршрутах: ");
        for (int i = 0; i < routes.size(); i++) {
            builder.append(routes.get(i).getNumber());
            if (i != (routes.size() - 1)) {
                builder.append(", ");
            }
        }
        builder.append(". Сначала отредактируйте маршруты");
        return builder.toString();
    }
}
