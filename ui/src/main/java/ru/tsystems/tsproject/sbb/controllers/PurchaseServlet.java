package ru.tsystems.tsproject.sbb.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by apple on 22.10.14.
 */
public class PurchaseServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(PurchaseServlet.class);

    @Inject private TicketService ticketService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TicketTO ticket  = (TicketTO) request.getSession().getAttribute("ticket");
            ticketService.AddTicket(ticket);
            request.setAttribute("isSuccess", true);
            request.getRequestDispatcher("/purchasesummary.jsp").forward(request, response);

        } catch (ServiceException ex) {
            LOGGER.error(ex);
            request.setAttribute("error", ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/index");
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
