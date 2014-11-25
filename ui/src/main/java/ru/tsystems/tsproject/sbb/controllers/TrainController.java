package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.services.TrainService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;
import ru.tsystems.tsproject.sbb.transferObjects.TripTO;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17.11.14.
 */

@Controller
@RequestMapping("/train")
public class TrainController {

    @Inject private TrainService trainService;
    @Inject private TripService tripService;

    private static final String ADDTRAIN = "train/addTrain";
    private static final String EDITTRAIN = "train/editTrain";

    @RequestMapping(method = RequestMethod.GET, value = "add")
    public String addTrain(Model model) {
        model.addAttribute("train", new TrainTO());
        try {
            model.addAttribute("rates", trainService.getTrainRates());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return ADDTRAIN;
    }

    @RequestMapping(method = RequestMethod.POST, value = "add")
    public String postAddTrain(Model model, @ModelAttribute("train") @Valid TrainTO trainTO, BindingResult result) {

            try {
                model.addAttribute("rates", trainService.getTrainRates());
                if (!result.hasErrors()) {
                    trainService.addTrain(trainTO);
                }
                else {
                    model.addAttribute("errors", result.getAllErrors());
                    return ADDTRAIN;
                }

            } catch (TrainAlreadyExistException e) {
                result.rejectValue("number", "error.train", "Поезд с таким номером уже существует в базе данных");
                try {
                    model.addAttribute("rates", trainService.getTrainRates());
                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
                model.addAttribute("errors", result.getAllErrors());
                return ADDTRAIN;
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return "redirect:getTrains";
        }


    @RequestMapping(method = RequestMethod.GET, value = "getTrains")
    public String getTrains(Model model) {
        try {
            model.addAttribute("trains", trainService.getTrains());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "train/getTrains";
    }

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public String editTrain(Model model, @PathVariable("id") Integer id) throws PageNotFoundException {
        try {
            TrainTO train = trainService.getTrain(id);
            model.addAttribute("train", train);
            model.addAttribute("rates", trainService.getTrainRates());
        } catch (TrainNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return EDITTRAIN;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public String postEditTrain( Model model, @ModelAttribute("train") @Valid TrainTO train, BindingResult result) {
        try {
            if (!result.hasErrors()) {
                trainService.editTrain(train);
                return "redirect:/main/train/getTrains";
            } else {
                model.addAttribute("rates", trainService.getTrainRates());
                model.addAttribute("errors", result.getAllErrors());
            }
        } catch (TrainAlreadyExistException ex) {
            result.rejectValue("number", "error.train", "Поезд с таким номером уже существует в базе данных");
            model.addAttribute("errors", result.getAllErrors());
            try {
                model.addAttribute("rates", trainService.getTrainRates());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return EDITTRAIN;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EDITTRAIN;
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteTrain(@PathVariable("id") Integer id, Model model) throws PageNotFoundException {
        try {
            List<TripTO> trips = tripService.getTripsByTrain(id);
            if (!trips.isEmpty()) {
                List<String> errors = new ArrayList<String>();
                StringBuilder builder = new StringBuilder("Поезд участвует в рейсах: ");
                for (int i = 0; i < trips.size(); i++) {
                    builder.append(trips.get(i).getNumber());
                    if (i != (trips.size() - 1)) {
                        builder.append(", ");
                    }
                }
                builder.append(". Сначала отредактируйте рейсы.");
                errors.add(builder.toString());
                model.addAttribute("errors", errors);
                model.addAttribute("trains", trainService.getTrains());
                return "train/getTrains";
            } else {
                TrainTO trainTO = trainService.getTrain(id);
                try {
                    trainService.deleteTrain(trainTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (TrainNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return "redirect:/main/train/getTrains";
    }
}
