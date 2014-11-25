package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.entity.User;

import java.util.Date;

/**
 * Created by apple on 11.11.14.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
    Long countByLogin(String login);
    Long countByNameAndSurnameAndBirthDate(String name, String surname, Date birthDate);

    @Query("select r from Role r where r.name = 'client'")
    Role getClientRole();
}
