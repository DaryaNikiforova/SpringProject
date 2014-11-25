package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.dao.UserDAO;
import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * Created by apple on 04.10.14.
 */

@Repository
public final class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {

        entityManager.persist(user);
    }

    @Override
    public Role getClientRole() {
        return (Role) entityManager.createQuery("select r from Role r where r.name = 'client'").getSingleResult();
    }

    @Override
    public User getUser(String login) {
        return (User) entityManager.createQuery("select u from User u where u.login = :login")
                .setParameter("login", login)
                .getSingleResult();
    }

    @Override
    public boolean isUserExist(String login) {

        return !entityManager.createQuery("select u from User u where u.login = :login")
                .setParameter("login", login)
                .getResultList()
                .isEmpty();
    }

    @Override
    public boolean isUserExist(String name, String surname, Date birthDate) {
        return !entityManager.createQuery("select u from User u where u.name = :name " +
                                          "and u.surname = :surname and u.birthDate = :birthDate")
                .setParameter("name", name)
                .setParameter("surname", surname)
                .setParameter("birthDate", birthDate)
                .getResultList()
                .isEmpty();
    }
}
