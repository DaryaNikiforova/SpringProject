package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.dao.TicketDAO;
import ru.tsystems.tsproject.sbb.database.entity.Ticket;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 04.10.14.
 */

@Repository

public final class TicketDAOImpl implements TicketDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addTicket(Ticket ticket) {
        entityManager.persist(ticket);
    }

    @Override
    public List<Ticket> getBoughtTickets(String stationFrom, String stationTo, int tripId) {
        return entityManager.createQuery("select t from Ticket t where t.trip.id = :id")
                            .setParameter("id", tripId).getResultList();
    }

    @Override
    public boolean isUserExist(String name, String surname, Date birthDate, int tripId) {
        return !entityManager.createQuery("select t from Ticket t where t.user.name = :name" +
                                         " and t.user.surname = :surname and t.user.birthDate = :date and t.trip.id = :tripId")
                             .setParameter("name", name).setParameter("surname", surname).setParameter("date", birthDate)
                             .setParameter("tripId", tripId)
                             .getResultList()
                             .isEmpty();
    }

    @Override
    public Ticket getTicket(String login, int tripId) {
        return (Ticket) entityManager.createQuery("select t from Ticket t where t.user.login = :login and t.trip.id = :tripId")
                .setParameter("login", login)
                .setParameter("tripId", tripId)
                .getSingleResult();
    }


    @Override
    public List<Ticket> getTicketsByTrip(int tripId) {
        return (List<Ticket>) entityManager.createQuery("select t from Ticket t where t.trip.id = :id")
                .setParameter("id", tripId)
                .getResultList();
    }


    /*@Override
    public boolean isUserRegistered(User user) {
        Integer result = (Integer) entityManager.createQuery("select count(t.id) from Ticket t " +
                                                             "where t.user.name=:name and t.user.surname = :surname " +
                                                             "and t.user.password=:password")
                                                .setParameter("name", user.getName())
                                                .setParameter("surname", user.getSurname())
                                                .setParameter("password", user.getPassword())
                                                .getSingleResult();
        return result>0;
    }*/
}
