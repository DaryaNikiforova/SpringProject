package ru.tsystems.tsproject.sbb.controllers.secure;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Gets information about routes and send it to client.
 * Includes checking of client input.
 * @author Daria Nikiforova
 */
public class GetRoutesServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GetRoutesServlet.class);

    @Inject private RouteService routeService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Method processes GET request.
     * @param request {@link HttpServletRequest} object contained client request
     * @param response {@link HttpServletResponse} object contained server respond
     * @throws ServletException when error was occurred inside method
     * @throws IOException if GET request could not be handled
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            List<RouteTO> routes = routeService.getAllRoutes();
            request.setAttribute("routes", routes);
            request.getRequestDispatcher("/secure/getRoutes.jsp").forward(request, response);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
