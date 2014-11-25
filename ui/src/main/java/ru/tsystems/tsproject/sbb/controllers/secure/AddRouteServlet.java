package ru.tsystems.tsproject.sbb.controllers.secure;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.controllers.BuyTicketServlet;
import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.mappers.MapperManager;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;
import ru.tsystems.tsproject.sbb.validation.ValidationManager;
import ru.tsystems.tsproject.sbb.validation.Validator;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gets information about route from client, send it to service for adding a new route.
 * Includes checking of client input.
 * @author Daria Nikiforova
 */
@Controller
@RequestMapping()
public class AddRouteServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(BuyTicketServlet.class);

    @Inject private StationService stationService;
    @Inject private RouteService routeService;
    /**
     * Method processes POST request.
     * @param request {@link HttpServletRequest} object contained client request
     * @param response {@link HttpServletResponse} object contained server respond
     * @throws ServletException when error was occurred inside method
     * @throws IOException if POST request could not be handled
     */
    //@RequestMapping(method = RequestMethod.POST, value = "/add")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

		try {
	        request.setAttribute("stations", stationService.getStations());


	        Mapper<RouteTO> mapper = MapperManager.getMapper(RouteTO.class);
	        RouteTO routeTO = mapper.map(request);
	        Validator<RouteTO> validator = ValidationManager.getValidator(routeTO);
	        if (validator.isValid()) {
	            routeService.addRoute(routeTO);
	            response.sendRedirect(request.getContextPath() + "/secure/getRoutes");
	        }
	        else {
	            request.setAttribute("errors", validator.getErrors());
	            request.getRequestDispatcher(request.getContextPath() + "/secure/addRoute.jsp").forward(request, response);
	        }		
        } catch (ServiceException ex) {
            LOGGER.error(ex);
            request.setAttribute("error", ex);
            request.getRequestDispatcher(request.getContextPath() + "/error.jsp").forward(request, response);
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

        try {
            request.setAttribute("stations", stationService.getStations());
            request.getRequestDispatcher("/secure/addRoute.jsp").forward(request, response);
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
