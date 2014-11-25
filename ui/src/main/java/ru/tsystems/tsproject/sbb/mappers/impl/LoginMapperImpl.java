package ru.tsystems.tsproject.sbb.mappers.impl;

import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.transferObjects.LoginTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by apple on 25.10.2014.
 */
public class LoginMapperImpl implements Mapper<LoginTO> {

    @Override
    public LoginTO map(HttpServletRequest request) {
        LoginTO loginTO = new LoginTO();
        loginTO.setLogin(request.getParameter("login").trim());
        loginTO.setPassword(request.getParameter("password"));
        return loginTO;
    }
}
