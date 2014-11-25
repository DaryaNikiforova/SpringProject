package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Ticket;

import java.util.Date;
import java.util.List;

/**
 * Created by apple on 14.10.14.
 */
public interface TicketDAO {
    void addTicket(Ticket ticket);
    List<Ticket> getBoughtTickets(String stationFrom, String stationTo, int tripId);
    boolean isUserExist(String name, String surname, Date birthDate, int tripId);
    Ticket getTicket(String login, int tripId);
    List<Ticket> getTicketsByTrip(int tripId);
}
