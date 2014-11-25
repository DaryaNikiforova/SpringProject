package ru.tsystems.tsproject.sbb.controllers.secure;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.helpers.NumberHelper;
import ru.tsystems.tsproject.sbb.transferObjects.PassengerTO;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Gets information about passengers, registered on trip, and send it to client.
 * Includes checking of client input.
 * @author Daria Nikiforova
 */
public class GetPassengersServlet extends HttpServlet {

    @Inject private TicketService ticketService;

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
            if(NumberHelper.tryParseInt(request.getParameter("tripId"))) {
                int tripId = Integer.parseInt(request.getParameter("tripId"));
                List<PassengerTO> passengerTOs = ticketService.getPassengersByTrip(tripId);
                request.setAttribute("passengers", passengerTOs);
                request.getRequestDispatcher("/secure/getPassengers.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/index");
            }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
