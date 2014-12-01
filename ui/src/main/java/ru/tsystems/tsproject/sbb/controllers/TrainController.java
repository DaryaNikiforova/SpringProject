package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tsystems.tsproject.sbb.exceptions.BadRequestException;
import ru.tsystems.tsproject.sbb.exceptions.PageNotFoundException;
import ru.tsystems.tsproject.sbb.responses.ErrorMessage;
import ru.tsystems.tsproject.sbb.responses.ValidationResponse;
import ru.tsystems.tsproject.sbb.services.TrainService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;
import ru.tsystems.tsproject.sbb.transferObjects.TripTO;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing train operations.
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
        model.addAttribute("rates", trainService.getTrainRates());
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
                model.addAttribute("rates", trainService.getTrainRates());
                model.addAttribute("errors", result.getAllErrors());
                return ADDTRAIN;
            }
            return "redirect:/main/train/";
        }


    @RequestMapping(method = RequestMethod.GET, value = "")
    public String getTrains(Model model) {
        model.addAttribute("trains", trainService.getTrains());
        return "train/getTrains";
    }

    @RequestMapping(method = RequestMethod.GET, value = "edit/{id}")
    public String editTrain(Model model, @PathVariable("id") String trainId) throws PageNotFoundException, BadRequestException {
        int id=0;
        try {
            id = Integer.parseInt(trainId);
            TrainTO train = trainService.getTrain(id);
            model.addAttribute("train", train);
            model.addAttribute("rates", trainService.getTrainRates());
        } catch (TrainNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        return EDITTRAIN;
    }

    @RequestMapping(method = RequestMethod.POST, value = "edit/{id}")
    public String postEditTrain(Model model, @ModelAttribute("train") @Valid TrainTO train, BindingResult result, @PathVariable("id") String trainId)
            throws PageNotFoundException, BadRequestException {
        int id=0;
        try {
            id = Integer.parseInt(trainId);
            train.setNumber(id);
            if (!result.hasErrors()) {
                trainService.editTrain(train);
                return "redirect:/main/train/";
            } else {
                model.addAttribute("rates", trainService.getTrainRates());
                model.addAttribute("errors", result.getAllErrors());
            }
        } catch (TrainNotFoundException e) {
            throw new PageNotFoundException(train.getNumber());
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        return EDITTRAIN;
    }

    @RequestMapping(value = "delete/{id}")
    public String deleteTrain(@PathVariable("id") String trainId, Model model) throws PageNotFoundException, BadRequestException {
        int id = 0;
        try {
            id = Integer.parseInt(trainId);
            List<TripTO> trips = tripService.getTripsByTrain(id);
            if (!trips.isEmpty()) {
                List<String> errors = new ArrayList<String>();
                errors.add(getDeleteErrorMessage(trips));
                model.addAttribute("errors", errors);
                model.addAttribute("trains", trainService.getTrains());
                return "train/getTrains";
            } else {
                TrainTO trainTO = trainService.getTrain(id);
                trainService.deleteTrain(trainTO);
            }
        } catch (TrainNotFoundException e) {
            throw new PageNotFoundException(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        return "redirect:/main/train/";
    }

    @RequestMapping(value = "delete/{id}.json")
    public @ResponseBody ValidationResponse deleteTrainAjax(@PathVariable("id") String trainId)
            throws PageNotFoundException, BadRequestException {
        ValidationResponse response = new ValidationResponse();
        List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
        int id = 0;
        try {
            id = Integer.parseInt(trainId);
            List<TripTO> trips = tripService.getTripsByTrain(id);
            if (!trips.isEmpty()) {
                errorMessages.add(new ErrorMessage("all", getDeleteErrorMessage(trips)));
                response.setStatus("FAIL");
            } else {
                TrainTO trainTO = trainService.getTrain(id);
                trainService.deleteTrain(trainTO);
                response.setStatus("SUCCESS");
            }
        }  catch (TrainNotFoundException e) {
            errorMessages.add(new ErrorMessage("all", "Ошибка при удалении"));
            response.setStatus("FAIL");
        } catch (NumberFormatException e) {
            errorMessages.add(new ErrorMessage("all", "Ошибка при удалении"));
            response.setStatus("FAIL");
        }
        response.setErrorMessageList(errorMessages);
        return response;
    }

    private String getDeleteErrorMessage(List<TripTO> trips) {
        StringBuilder builder = new StringBuilder("Поезд участвует в рейсах: ");
        for (int i = 0; i < trips.size(); i++) {
            builder.append(trips.get(i).getId());
            if (i != (trips.size() - 1)) {
                builder.append(", ");
            }
        }
        builder.append(". Сначала отредактируйте рейсы.");
        return builder.toString();
    }
}
