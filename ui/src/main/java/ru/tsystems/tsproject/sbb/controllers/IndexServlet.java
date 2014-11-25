package ru.tsystems.tsproject.sbb.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Rin on 24.10.2014.
 */
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("login") != null) {
            String role = (String) session.getAttribute("role");
            request.setAttribute("showAdmin", role.equals("admin"));
            request.setAttribute("loggedIn", true);
        }
        request.setAttribute("page", "index");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

}
