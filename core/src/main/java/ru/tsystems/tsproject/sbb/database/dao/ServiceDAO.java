package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Service;

import java.util.List;

/**
 * Created by apple on 22.10.14.
 */
public interface ServiceDAO {
    public List<Service> getServices();
    public double getValueById(int id);
    public Service getServiceById(int id);
}
