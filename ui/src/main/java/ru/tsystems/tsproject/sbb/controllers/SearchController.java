package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.services.exceptions.TripNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.SearchStationTO;
import ru.tsystems.tsproject.sbb.transferObjects.SearchTO;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;
import ru.tsystems.tsproject.sbb.transferObjects.TimetableTO;

import javax.inject.Inject;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by apple on 13.11.14.
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    private static final String SEARCHSTATIONTRIP = "search/searchstationtrip";
    private static final String SEARCHTRIP = "search/searchtrip";
    @Inject private StationService stationService;
    @Inject private TripService tripService;

    @RequestMapping(method = RequestMethod.GET, value = "byStation")
    public String searchByStation(Model model) {
        model.addAttribute("searchStation", new SearchStationTO());
        return SEARCHSTATIONTRIP;
    }

    @RequestMapping(method = RequestMethod.POST, value="byStation")
    public String getTripsByStation(Model model,
                                    @ModelAttribute("searchStation") @Valid SearchStationTO searchStation,
                                    BindingResult result) throws PageNotFoundException {
        if (!result.hasErrors()) {
            List<TimetableTO> timetableList = null;
            try {
                timetableList = tripService.getRoutesByStation(
                searchStation.getStation(),
                new SimpleDateFormat("dd.MM.yyyy").parse(searchStation.getDate()));
            }
            catch (ParseException e) {
                result.rejectValue("date", "error.searchStation", "Дата должна быть в формате 'дд.мм.гггг'");
                model.addAttribute("errors", result.getAllErrors());
            } catch (StationNotFoundException e) {
                result.rejectValue("date", "error.searchStation", "Задана не известная станция");
                model.addAttribute("errors", result.getAllErrors());
            }
            model.addAttribute("stationTimetable", timetableList);
            model.addAttribute("isPost", true);
        } else {
            model.addAttribute("errors", result.getAllErrors());
        }

        return SEARCHSTATIONTRIP;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String search(Model model) {
        model.addAttribute("searchObject", new SearchTO());
        return SEARCHTRIP;
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public String getTrips(Model model, @ModelAttribute("searchObject") @Valid SearchTO searchObject, BindingResult result) {
        if (!result.hasErrors()) {
            List<TimetableTO> timetableList = null;
            try {
                timetableList = tripService.searchTrains(
                        searchObject.getStationFrom(),
                        searchObject.getStationTo(),
                        new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(searchObject.getDeparture()),
                        new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(searchObject.getArrival()));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (StationNotFoundException e) {
                result.rejectValue("date", "error.searchStation", "Задана не известная станция");
                model.addAttribute("errors", result.getAllErrors());
            } catch (TripNotFoundException e) {
                e.printStackTrace();
            }
            model.addAttribute("timetable", timetableList);
            model.addAttribute("isPost", true);
        }
        else {
            model.addAttribute("errors", result.getAllErrors());
        }

        return SEARCHTRIP;
    }

    @ModelAttribute("stations")
    public List<StationTO> getStations() {
        List<StationTO> stations = stationService.getStations();
        return stations;
    }
}
