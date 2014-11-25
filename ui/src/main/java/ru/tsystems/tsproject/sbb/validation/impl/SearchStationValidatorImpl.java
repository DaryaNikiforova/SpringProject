package ru.tsystems.tsproject.sbb.validation.impl;

import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.transferObjects.SearchStationTO;
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
public class SearchStationValidatorImpl extends Validator<SearchStationTO> {
    private static final Pattern stationPattern = Pattern.compile("[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]", Pattern.UNICODE_CASE);
    private static final Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}", Pattern.UNICODE_CASE);

    public SearchStationValidatorImpl(SearchStationTO obj) {
        super(obj);
    }

    @Override
    protected Map<String, String> validate(SearchStationTO searchStationTO) {
        Map<String, String> errors = new HashMap<String, String>();
        if (searchStationTO.getStation() == null || searchStationTO.getStation().isEmpty()) {
            errors.put("station", StringHelper.encode("поле должно быть заполнено"));
        } else if (!stationPattern.matcher(searchStationTO.getStation()).matches()) {
            errors.put("station", StringHelper.encode(
                    "станция не должна содержать недопустимых символов"));
        } else if (searchStationTO.getStation().length() < 3 || searchStationTO.getStation().length() > 100) {
            errors.put("station", StringHelper.encode(
                    "название станции не должно состоять из меньше чем 3 и больше чем 100 символов"));
        }
        if (searchStationTO.getDate() == null || searchStationTO.getDate().isEmpty()) {
            errors.put("date", StringHelper.encode("поле должно быть заполнено"));
        } else if (!datePattern.matcher(searchStationTO.getDate()).matches()) {
            errors.put("date", StringHelper.encode("неверный формат даты"));
        } else {
            try {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(searchStationTO.getDate());
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("dd.MM.yyyy");
                String today = sdf.format(new Date());
                Date minDate = sdf.parse(today);
                Calendar c = Calendar.getInstance();
                c.setTime(minDate);
                c.add(Calendar.DATE, 45);
                Date maxDate = c.getTime();
                if (date.before(minDate)) {
                    errors.put("arrival", StringHelper.encode("дата не должна быть прошлой"));
                } else if (date.after(maxDate)) {
                    errors.put("arrival", StringHelper.encode("дата не должна превышать 45 дней от настоящей даты"));
                }

            } catch (ParseException ex) {
                errors.put("departure", StringHelper.encode("неверный формат даты"));
            }
        }
        return errors;
    }
}
