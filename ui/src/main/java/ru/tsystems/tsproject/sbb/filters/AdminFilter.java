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

public class AdminFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AdminFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        LOGGER.debug("Start AdminFilter for user role checking");
        LOGGER.info("AdminFilter starting");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        boolean isAdmin = true;
        if (request.isRequestedSessionIdValid()) {
            HttpSession session = request.getSession(false);
            if (session.getAttribute("role") != null) {
                if (session.getAttribute("role").equals("admin")) {
                    req.setCharacterEncoding("UTF-8");
                    resp.setCharacterEncoding("UTF-8");
                    chain.doFilter(req, resp);
                } else {
                    isAdmin = false;
                }
            } else {
                isAdmin = false;
            }
        } else {
            isAdmin = false;
        }
        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        LOGGER.debug("AdminFilter completed");
    }

    public void init(FilterConfig config) throws ServletException {
    }
}
