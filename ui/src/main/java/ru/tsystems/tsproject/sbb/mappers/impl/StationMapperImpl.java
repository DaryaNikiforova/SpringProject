package ru.tsystems.tsproject.sbb.mappers.impl;
import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;
import javax.servlet.http.HttpServletRequest;

public class StationMapperImpl implements Mapper<StationTO> {
    @Override
    public StationTO map(HttpServletRequest request) {
        //StationTO station = new StationTO(request.getParameter("name").trim());
        return null;
    }
}
