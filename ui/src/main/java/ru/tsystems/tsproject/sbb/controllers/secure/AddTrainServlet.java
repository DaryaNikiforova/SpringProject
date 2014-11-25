package ru.tsystems.tsproject.sbb.controllers.secure;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.mappers.MapperManager;
import ru.tsystems.tsproject.sbb.services.TrainService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainAlreadyExistException;
import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;
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
 * Gets information about train from client, send it to service for adding a new train.
 * Includes checking of client input.
 * @author Daria Nikiforova
 */
public class AddTrainServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddTrainServlet.class);

    @Inject private TrainService trainService;
    /**
     * Method processes GET request.
     * @param request {@link HttpServletRequest} object contained client request
     * @param response {@link HttpServletResponse} object contained server respond
     * @throws ServletException when error was occurred inside method
     * @throws IOException if GET request could not be handled
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

         try {
	        request.setAttribute("rates", trainService.getTrainRates());

	        Mapper<TrainTO> mapper = MapperManager.getMapper(TrainTO.class);
	        TrainTO trainTO = mapper.map(request);
	        Validator<TrainTO> validator = ValidationManager.getValidator(trainTO);
	        if (validator.isValid()) {
	            trainService.addTrain(trainTO);
	            response.sendRedirect(request.getContextPath() + "/secure/getTrains");
	        }
	        else {
	            request.setAttribute("errors", validator.getErrors());
	            request.getRequestDispatcher("/secure/addTrain.jsp").forward(request, response);
	        }
         } catch (TrainAlreadyExistException ex) {
             Map<String, String> errors = new HashMap<String, String>();
             errors.put("number", StringHelper.encode("Станция с таким именем уже существует"));
             request.setAttribute("errors", errors);
             request.getRequestDispatcher("/secure/addTrain.jsp").forward(request, response);
         } catch (ServiceException ex) {
             LOGGER.error(ex);
             request.setAttribute("error", ex);
             request.getRequestDispatcher("/error.jsp").forward(request, response);
         }
    }

    /**
     * Method processes POST request.
     * @param request {@link HttpServletRequest} object contained client request
     * @param response {@link HttpServletResponse} object contained server respond
     * @throws ServletException when error was occurred inside method
     * @throws IOException if POST request could not be handled
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("rates", trainService.getTrainRates());
			request.getRequestDispatcher("/secure/addTrain.jsp").forward(request, response);
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
