package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.entity.User;
import ru.tsystems.tsproject.sbb.database.repositories.UserRepository;
import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyExistException;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Implements methods interacting with DAO methods. Needs to get and change data in database.
 * @author Daria Nikiforova
 */

@Service
@Transactional
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Inject UserRepository userRepository;

    /**
     * Adds user to database. Throws exception if database connection is lost,
     * bad request or error with transaction. Throws UserAlreadyExistException if
     * trying to add existing user.
     * @param userTO
     * @throws UserAlreadyExistException
     */

    public void addUser(UserTO userTO) throws UserAlreadyExistException, ParseException {
        Role role = userRepository.getClientRole();
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(userTO.getBirthDate());
        if (userRepository.countByLogin(userTO.getLogin()) == 0
           && userRepository.countByNameAndSurnameAndBirthDate(userTO.getName(), userTO.getSurname(), date) == 0) {
            User user = new User();
            user.setName(userTO.getName());
            user.setSurname(userTO.getSurname());
            user.setBirthDate(date);
            user.setLogin(userTO.getLogin());
            user.setPassword(userTO.getPassword());
            user.setRole(role);
            userRepository.save(user);
            LOGGER.info("Пользователь добавлен в базу данных");
        } else {
            LOGGER.error("Пользователь с логином " + userTO.getLogin() + " уже существует в базе данных");
            throw new UserAlreadyExistException("Пользователь с логином " + userTO.getLogin() + " уже существует в базе данных");
        }
    }
}
