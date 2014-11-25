package ru.tsystems.tsproject.sbb.mappers.impl;

import ru.tsystems.tsproject.sbb.mappers.Mapper;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by apple on 23.10.2014.
 */
public class UserMapperImpl implements Mapper<UserTO> {
    @Override
    public UserTO map(HttpServletRequest request) {
        UserTO user = new UserTO();
        user.setLogin(request.getParameter("login").trim());
        user.setPassword(request.getParameter("password"));
        user.setName(request.getParameter("name").trim());
        user.setSurname(request.getParameter("surname").trim());
        user.setBirthDate(request.getParameter("birth"));
        return user;
    }
}
