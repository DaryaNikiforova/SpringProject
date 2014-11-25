package ru.tsystems.tsproject.sbb.validation.impl;

import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.transferObjects.SearchTO;
import ru.tsystems.tsproject.sbb.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by apple on 25.10.2014.
 */
public class SearchValidatorImpl extends Validator<SearchTO> {
    private static final Pattern stationPattern = Pattern.compile("[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]", Pattern.UNICODE_CASE);
    private static final Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}", Pattern.UNICODE_CASE);

    public SearchValidatorImpl(SearchTO obj) {
        super(obj);
    }

    @Override
    protected Map<String, String> validate(SearchTO searchTO) {
        Map<String, String> errors = new HashMap<String, String>();
        if (searchTO.getStationFrom() == null || searchTO.getStationFrom().isEmpty()) {
            errors.put("stationFrom", StringHelper.encode("поле должно быть заполнено"));
        } else if (!stationPattern.matcher(searchTO.getStationFrom()).matches()) {
            errors.put("stationFrom", StringHelper.encode(
                    "станция не должна содержать недопустимых символов"));
        } else if (searchTO.getStationFrom().length() < 3 || searchTO.getStationFrom().length() > 100) {
            errors.put("stationFrom", StringHelper.encode(
                    "название станции не должно состоять из меньше чем 3 и больше чем 100 символов"));
        }
        if (searchTO.getStationTo() == null || searchTO.getStationTo().isEmpty()) {
            errors.put("stationTo", StringHelper.encode("поле должно быть заполнено"));
        } else if (!stationPattern.matcher(searchTO.getStationTo()).matches()) {
            errors.put("stationTo", StringHelper.encode(
                    "станция не должна содержать недопустимых символов"));
        } else if (searchTO.getStationTo().length() < 3 || searchTO.getStationTo().length() > 100) {
            errors.put("stationTo", StringHelper.encode(
                    "название станции не должно состоять из меньше чем 3 и больше чем 100 символов"));
        }
        if (!searchTO.getStationFrom().isEmpty() && searchTO.getStationFrom().equals(searchTO.getStationTo())) {
            errors.put("stations", StringHelper.encode("выбранные станции не должны совпадать"));
        }

        Date departureDate = null;
        Date arrivalDate = null;

        if (searchTO.getDeparture() == null || searchTO.getDeparture().isEmpty()) {
            errors.put("departure", StringHelper.encode("поле должно быть заполнено"));
        } else if (!datePattern.matcher(searchTO.getDeparture()).matches()) {
            errors.put("departure", StringHelper.encode("неверный формат даты"));
        } else {
            try {
                departureDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(searchTO.getDeparture());
            } catch (ParseException ex) {
                errors.put("departure", StringHelper.encode("неверный формат даты"));
            }
        }
        if (searchTO.getArrival() == null || searchTO.getArrival().isEmpty()) {
            errors.put("arrival", StringHelper.encode("поле должно быть заполнено"));
        } else if (!datePattern.matcher(searchTO.getArrival()).matches()) {
            errors.put("arrival", StringHelper.encode("неверный формат даты"));
        } else {
            try {
                arrivalDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(searchTO.getArrival());
            } catch (ParseException ex) {
                errors.put("arrival", StringHelper.encode("неверный формат даты"));
            }
        }

        Date minDate = null;
        Date maxDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd.MM.yyyy");
            String today = sdf.format(new Date());
            minDate = sdf.parse(today);
            Calendar c = Calendar.getInstance();
            c.setTime(minDate);
            c.add(Calendar.DATE, 45);
            maxDate = c.getTime();
        } catch (ParseException ex) {
            errors.put("arrival", StringHelper.encode("неверный формат даты"));
        }

        if (departureDate != null) {
            if (departureDate.before(minDate)) {
                errors.put("departure", StringHelper.encode("дата не должна быть прошлой"));
            } else if (departureDate.after(maxDate)) {
                errors.put("departure", StringHelper.encode("дата не должна превышать 45 дней от настоящей даты"));
            }
        }
        if (arrivalDate != null) {
            if (arrivalDate.before(minDate)) {
                errors.put("arrival", StringHelper.encode("дата не должна быть прошлой"));
            } else if (arrivalDate.after(maxDate)) {
                errors.put("arrival", StringHelper.encode("дата не должна превышать 45 дней от настоящей даты"));
            }
        }
        if (departureDate != null && arrivalDate != null) {
            if (arrivalDate.before(departureDate) || arrivalDate.equals(departureDate)) {
                errors.put("dates", StringHelper.encode("дата \"по\" должна быть больше даты \"с\""));
            }
        }

        return errors;
    }
}
