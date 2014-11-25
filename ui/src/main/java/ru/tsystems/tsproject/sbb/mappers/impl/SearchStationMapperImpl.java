package ru.tsystems.tsproject.sbb.mappers.impl;

import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.transferObjects.SearchStationTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by apple on 25.10.2014.
 */
public class SearchStationMapperImpl implements Mapper<SearchStationTO> {
    @Override
    public SearchStationTO map(HttpServletRequest request) {
        SearchStationTO searchStationTO = new SearchStationTO();
        searchStationTO.setDate(request.getParameter("date"));
        searchStationTO.setStation(request.getParameter("station"));
        return searchStationTO;
    }
}
