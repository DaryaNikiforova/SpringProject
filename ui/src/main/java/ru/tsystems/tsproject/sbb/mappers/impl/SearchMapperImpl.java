package ru.tsystems.tsproject.sbb.mappers.impl;

import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.transferObjects.SearchTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by apple on 25.10.2014.
 */
public class SearchMapperImpl implements Mapper<SearchTO> {

    @Override
    public SearchTO map(HttpServletRequest request) {
        SearchTO searchTO = new SearchTO();
        searchTO.setStationFrom(request.getParameter("stationFrom").trim());
        searchTO.setStationTo(request.getParameter("stationTo").trim());
        searchTO.setDeparture(request.getParameter("departure"));
        searchTO.setArrival(request.getParameter("arrival"));

        return searchTO;
    }
}
