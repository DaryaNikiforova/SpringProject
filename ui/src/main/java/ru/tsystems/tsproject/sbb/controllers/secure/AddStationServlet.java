package ru.tsystems.tsproject.sbb.controllers.secure;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.controllers.BuyTicketServlet;
import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.mappers.MapperManager;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationAlreadyExistException;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;
import ru.tsystems.tsproject.sbb.validation.ValidationManager;
import ru.tsystems.tsproject.sbb.validation.Validator;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gets information about station from client, send it to service for adding a new station.
 * Includes checking of client input.
 * @author Daria Nikiforova
 */
public class AddStationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BuyTicketServlet.class);

    /**
     * Method processes POST request.
     * @param request {@link HttpServletRequest} object contained client request
     * @param response {@link HttpServletResponse} object contained server respond
     * @throws ServletException when error was occurred inside method
     * @throws IOException if POST request could not be handled
     */

    @Inject
    private StationService stationService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

		try {
	        Mapper<StationTO> mapper = MapperManager.getMapper(StationTO.class);
	        StationTO stationTO = mapper.map(request);
	        Validator<StationTO> validator = ValidationManager.getValidator(stationTO);
	        if (validator.isValid()) {
	            stationService.addStation(stationTO);
	            response.sendRedirect(request.getContextPath() + "/secure/getStations");
	        }
	        else {
                request.setAttribute("errors", validator.getErrors());
                request.getRequestDispatcher( "/secure/addStation.jsp").forward(request, response);
            }
        } catch (StationAlreadyExistException ex) {
            Map<String, String> errors = new HashMap<String, String>();
            errors.put("name", StringHelper.encode("Станция с таким именем уже существует"));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/secure/addStation.jsp").forward(request, response);
        } catch (ServiceException ex) {
            LOGGER.error(ex);
            request.setAttribute("error", ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Method processes GET request.
     * @param request {@link HttpServletRequest} object contained client request
     * @param response {@link HttpServletResponse} object contained server respond
     * @throws ServletException when error was occurred inside method
     * @throws IOException if GET request could not be handled
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/secure/addStation.jsp").forward(request, response);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

}
