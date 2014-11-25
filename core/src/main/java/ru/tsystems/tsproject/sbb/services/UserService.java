package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.entity.User;
import ru.tsystems.tsproject.sbb.database.repositories.UserRepository;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.UserNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
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

    public void addUser(UserTO userTO) throws UserAlreadyExistException {        
        try {
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
                LOGGER.error("Невозможно добавить пользователя");
                throw new UserAlreadyExistException("User already exist in database");
            }
        } catch (ParseException e) {
            LOGGER.error("Некорректный формат данных");
        } catch (NullPointerException e) {
            LOGGER.error("Попытка некорректного ввода в поле данных");
            throw new NullPointerException("Попытка некорректного ввода в поле данных");
        }
    }

    /**
     * Returns user object. Throws exception if database connection is lost,
     * bad request or error with transaction. Throws UserNotFoundException when user doesn't
     * exist in database.
     * @param login users login.
     * @return founded user.
     * @throws ServiceException
     */

    public UserTO getUser(String login) throws ServiceException {
        User user = null;
        UserTO userTO = new UserTO();
        try {
            LOGGER.info("Получение информации о пользователе из базы данных");
            if (userRepository.countByLogin(login) != 0) {
                user = userRepository.findByLogin(login);
            }
            else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        } catch (PersistenceException ex) {
            LOGGER.error("Невозможно получить информацию о пользователе");
            throw new ServiceException("Невозможно получить информацию о пользователе");
        }

        if (user != null) {
            userTO.setLogin(user.getLogin());
            userTO.setRole(user.getRole().getName());
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd.MM.yyyy");
            String d = sdf.format(user.getBirthDate());
            userTO.setName(user.getName());
            userTO.setSurname(user.getSurname());
            userTO.setPassword(user.getPassword());
            userTO.setBirthDate(d);
        }
        return userTO;
    }
}
