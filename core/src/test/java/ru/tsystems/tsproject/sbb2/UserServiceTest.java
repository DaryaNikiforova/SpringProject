package ru.tsystems.tsproject.sbb2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.entity.User;
import ru.tsystems.tsproject.sbb.database.repositories.UserRepository;
import ru.tsystems.tsproject.sbb.services.UserService;
import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyExistException;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
* Implements set of tests for User service.
* @author Daria Nikiforova
*/
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService = new UserService();

    UserTO defaultUserTo;
    User defaultUser;
    Date defaultDate;
    Role clientRole;

    @Before
    public void setup() throws Exception {
        clientRole = new Role();
        clientRole.setId(2);
        clientRole.setName("client");

        String name = "Jack";
        String surname = "London";
        String birthDate = "21.05.1860";
        String login = "jack";
        String password = "London";

        defaultUserTo = new UserTO();
        defaultUserTo.setName(name);
        defaultUserTo.setSurname(surname);
        defaultUserTo.setPassword(password);
        defaultUserTo.setLogin(login);
        defaultUserTo.setBirthDate(birthDate);

        defaultDate = new SimpleDateFormat("dd.MM.yyyy").parse(defaultUserTo.getBirthDate());

        defaultUser = new User();
        defaultUser.setRole(clientRole);
        defaultUser.setPassword(password);
        defaultUser.setBirthDate(defaultDate);
        defaultUser.setName(name);
        defaultUser.setSurname(surname);
        defaultUser.setLogin(login);
        defaultUser.setId(0);
    }

    @Test
    public void testAddUser_Success() throws Exception {
        when(userRepository.getClientRole()).thenReturn(clientRole);
        when(userRepository.countByLogin(defaultUserTo.getLogin())).thenReturn(new Long(0));
        when(userRepository.countByNameAndSurnameAndBirthDate(defaultUserTo.getName(), defaultUserTo.getSurname(), defaultDate))
                .thenReturn(new Long(0));
        userService.addUser(defaultUserTo);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User user = captor.getValue();
        assertEquals(user.getLogin(), defaultUserTo.getLogin());
        assertEquals(user.getName(), defaultUserTo.getName());
        assertEquals(user.getSurname(), defaultUserTo.getSurname());
        assertEquals(user.getPassword(), defaultUserTo.getPassword());
        assertEquals(user.getBirthDate(), defaultDate);
        assertEquals(user.getRole(), clientRole);
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testAddUser_UserAlreadyExistException() throws Exception {
        when(userRepository.countByLogin(anyString())).thenReturn(new Long(1));
        when(userRepository.countByNameAndSurnameAndBirthDate(anyString(), anyString(), eq(defaultDate))).thenReturn(new Long(1));
        userService.addUser(defaultUserTo);
    }

    @Test(expected = ParseException.class)
    public void testAddUser_ParseException() throws Exception {
        userService.addUser(WithChangedData("wrong date"));
    }

    private UserTO WithChangedData(String date) {
        UserTO userTO = new UserTO();
        userTO.setName(defaultUserTo.getName());
        userTO.setSurname(defaultUserTo.getSurname());
        userTO.setBirthDate(date);
        userTO.setPassword(defaultUserTo.getPassword());
        userTO.setLogin(defaultUserTo.getLogin());
        userTO.setRole(defaultUserTo.getRole());
        return userTO;
    }
}
