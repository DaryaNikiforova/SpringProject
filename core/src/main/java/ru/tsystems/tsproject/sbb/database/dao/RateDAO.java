package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Rate;

import java.util.List;

/**
 * Created by apple on 22.10.14.
 */
public interface RateDAO {
    public List<Rate> getRates();
    public List<Rate> getTrainRates();
    public double getValueById(int id);
}
