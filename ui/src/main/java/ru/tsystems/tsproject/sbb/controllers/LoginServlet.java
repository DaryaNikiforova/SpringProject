package ru.tsystems.tsproject.sbb.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.mappers.MapperManager;
import ru.tsystems.tsproject.sbb.services.UserService;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.UserNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.LoginTO;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;
import ru.tsystems.tsproject.sbb.validation.ValidationManager;
import ru.tsystems.tsproject.sbb.validation.Validator;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 14.10.14.
 */
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class);

    @Inject private UserService userService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("login") != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
        try {
            Mapper<LoginTO> mapper = MapperManager.getMapper(LoginTO.class);
            LoginTO loginTO = mapper.map(request);
            Validator<UserTO> validator = ValidationManager.getValidator(loginTO);
            if (validator.isValid()) {
                UserTO user = userService.getUser(loginTO.getLogin());
                if (user.getPassword().equals(loginTO.getPassword())) {
                    LOGGER.info("Вход пользователя " + user.getLogin() + "в систему");
                    session = request.getSession();
                    session.setAttribute("role", user.getRole());
                    session.setAttribute("login", user.getLogin());
                    //session.removeAttribute("redirect");
                    response.sendRedirect(request.getContextPath() + "/index");
                } else throw new UserNotFoundException("");
            } else {
                request.setAttribute("errors", validator.getErrors());
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (ServiceException ex) {
            Map<String, String> errors = new HashMap<String, String>();
            errors.put("password", StringHelper.encode("Такой комбинации логина и пароля не существует"));
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("login") != null) {
            response.sendRedirect(request.getContextPath() + "/index");
        } else {
            request.setAttribute("page", "login");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
