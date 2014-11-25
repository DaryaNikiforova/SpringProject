package ru.tsystems.tsproject.sbb.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.mappers.MapperManager;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.transferObjects.SearchTO;
import ru.tsystems.tsproject.sbb.transferObjects.TimetableTO;
import ru.tsystems.tsproject.sbb.validation.ValidationManager;
import ru.tsystems.tsproject.sbb.validation.Validator;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by apple on 20.10.14.
 */
public class SearchTripServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SearchTripServlet.class);

    @Inject private StationService stationService;
    @Inject private TripService tripService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("isPost", true);
        try {
            request.setAttribute("stations", stationService.getStations());

            Mapper<SearchTO> mapper = MapperManager.getMapper(SearchTO.class);
            SearchTO searchTO = mapper.map(request);
            Validator<SearchTO> validator = ValidationManager.getValidator(searchTO);
            if (validator.isValid()) {
                List<TimetableTO> timetableList = tripService.searchTrains(
                        searchTO.getStationFrom(),
                        searchTO.getStationTo(),
                        new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(searchTO.getDeparture()),
                        new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(searchTO.getArrival()));
                request.setAttribute("timetable",timetableList);
                request.getRequestDispatcher("/searchtrip.jsp").forward(request, response);
            } else {
                request.setAttribute("errors", validator.getErrors());
                request.getRequestDispatcher("/searchtrip.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            LOGGER.error(ex);
            request.setAttribute("error", "Ошибка приложения");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LOGGER.info("TripSearching Servlet");
            request.setAttribute("stations", stationService.getStations());
            request.getRequestDispatcher("/searchtrip.jsp").forward(request, response);
        } catch (ServiceException ex) {
            LOGGER.error(ex);
            request.setAttribute("error", ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
