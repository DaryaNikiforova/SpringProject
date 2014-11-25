package ru.tsystems.tsproject.sbb.mappers.impl;

import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.services.helpers.NumberHelper;
import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by apple on 22.10.2014.
 */
public class TrainMapperImpl implements Mapper<TrainTO> {

    @Override
    public TrainTO map(HttpServletRequest request) {
        TrainTO train = new TrainTO();
        train.setName(request.getParameter("name").trim());
        String number = request.getParameter("number");
        String seatCounter = request.getParameter("seatCount");
        String rate = request.getParameter("rate");
        train.setNumber(number != null && !number.isEmpty() && NumberHelper.tryParseInt(number) ? Integer.parseInt(number) : -1);
        train.setSeatCount(seatCounter != null && !seatCounter.isEmpty() && NumberHelper.tryParseInt(seatCounter)? Integer.parseInt(seatCounter) : -1);
        train.setRateId(rate != null && !rate.isEmpty() && NumberHelper.tryParseInt(rate)? Integer.parseInt(rate) : -1);

        return train;
    }
}
