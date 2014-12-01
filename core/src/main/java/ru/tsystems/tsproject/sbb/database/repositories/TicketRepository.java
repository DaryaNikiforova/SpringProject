package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsystems.tsproject.sbb.database.entity.Ticket;

import java.util.Date;
import java.util.List;

/**
 * Created by apple on 11.11.14.
 */
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Long countByUser_NameAndUser_SurnameAndUser_BirthDateAndTrip_Id
            (String name, String surname, Date birthDate, int tripId);
    List<Ticket> findByTrip_Id(int tripId);
    Long countByTripIdAndSeat(int tripId, int seat);
    List<Ticket> findByUser_Id(int userId);
}
