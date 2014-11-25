package ru.tsystems.tsproject.sbb.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by apple on 15.10.14.
 */
public class AuthFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        LOGGER.debug("Start AuthFilter for user login checking");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        if (session.getAttribute("login") != null) {
            resp.setCharacterEncoding("UTF-8");
            req.setCharacterEncoding("UTF-8");
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        LOGGER.debug("AuthFilter completed");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
