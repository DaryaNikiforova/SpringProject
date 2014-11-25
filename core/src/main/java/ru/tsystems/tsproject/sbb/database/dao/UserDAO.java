package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.entity.User;

import java.util.Date;

/**
 * Created by apple on 14.10.14.
 */
public interface UserDAO {
    void addUser(User user);

    Role getClientRole();

    User getUser(String login);

    boolean isUserExist(String login);

    boolean isUserExist(String name, String surname, Date birthDate);
}
