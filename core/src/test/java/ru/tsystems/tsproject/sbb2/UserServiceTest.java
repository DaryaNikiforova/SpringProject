//package ru.tsystems.tsproject.sbb2;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import ru.tsystems.tsproject.sbb.database.dao.FactoryDAO;
//import ru.tsystems.tsproject.sbb.database.dao.UserDAO;
//import ru.tsystems.tsproject.sbb.database.entity.Role;
//import ru.tsystems.tsproject.sbb.database.entity.User;
//import ru.tsystems.tsproject.sbb.services.UserService;
//import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyExistException;
//import ru.tsystems.tsproject.sbb.services.exceptions.UserNotFoundException;
//import ru.tsystems.tsproject.sbb.transferObjects.UserTO;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static org.mockito.Mockito.when;
//
///**
// * Implements set of tests for User service.
// * @author Daria Nikiforova
// */
//@RunWith(MockitoJUnitRunner.class)
//public class class {
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private FactoryDAO factoryDAO;
//
//    @Mock
//    private UserDAO userDAO;
//
//    @Mock
//    private EntityTransaction entityTransaction;
//
//    @InjectMocks
//    private UserService userService = new UserService(null, (UserDAO)null);
//
//    /**
//     * Tests method UserService.addUser. Test passed when expected data equals to created.
//     * @throws Exception
//     */
//    @Test
//    public void testAddUser_Success() throws Exception {
//        String name = "Jack";
//        String surname = "London";
//        String birthDate = "21.05.1860";
//        String login = "jack";
//        String password = "London";
//
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//
//        UserTO user = new UserTO();
//        user.setName(name);
//        user.setSurname(surname);
//        user.setPassword(password);
//        user.setLogin(login);
//        user.setBirthDate(birthDate);
//
//        User expectedUser = new User();
//        expectedUser.setName(name);
//        expectedUser.setLogin(login);
//        expectedUser.setPassword(password);
//        expectedUser.setRole(role);
//        expectedUser.setSurname(surname);
//        expectedUser.setId(1);
//
//        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(birthDate);
//
//        when(userDAO.getClientRole()).thenReturn(role);
//        when(userDAO.isUserExist(login)).thenReturn(false);
//        when(userDAO.isUserExist(name, surname, date)).thenReturn(false);
//        when(userDAO.getUser(login)).thenReturn(expectedUser);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//
//        userService.addUser(user);
//        User createdUser = userDAO.getUser(login);
//        //verify(userDAO).addUser(expectedUser);
//        Assert.assertEquals(expectedUser.getId(), createdUser.getId());
//    }
//
//    /**
//     * Tests method UserService.addUser. Test passed when throws UserAlreadyExistException.
//     * @throws Exception
//     */
//    @Test(expected = UserAlreadyExistException.class)
//    public void testAddUser_UserAlreadyExistException() throws Exception {
//        String name = "Jack";
//        String surname = "London";
//        String birthDate = "21.05.1860";
//        String login = "jack";
//        String password = "London";
//
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//
//        UserTO user = new UserTO();
//        user.setName(name);
//        user.setSurname(surname);
//        user.setPassword(password);
//        user.setLogin(login);
//        user.setBirthDate(birthDate);
//
//        User expectedUser = new User();
//        expectedUser.setName(name);
//        expectedUser.setLogin(login);
//        expectedUser.setPassword(password);
//        expectedUser.setRole(role);
//        expectedUser.setSurname(surname);
//        expectedUser.setId(1);
//
//        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(birthDate);
//
//        when(userDAO.getClientRole()).thenReturn(role);
//        when(userDAO.isUserExist(login)).thenReturn(true);
//        when(userDAO.isUserExist(name, surname, date)).thenReturn(true);
//        when(userDAO.getUser(login)).thenReturn(expectedUser);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//
//        userService.addUser(user);
//    }
//
//    /**
//     * Tests method UserService.addUser. Test passed when throws ParseException.
//     * @throws Exception
//     */
//    @Test(expected = ParseException.class)
//    public void testAddUser_ServiceException() throws Exception {
//        String name = "Jack";
//        String surname = "London";
//        String birthDate = "aaaa";
//        String login = "jack";
//        String password = "London";
//
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//
//        UserTO user = new UserTO();
//        user.setName(name);
//        user.setSurname(surname);
//        user.setPassword(password);
//        user.setLogin(login);
//        user.setBirthDate(birthDate);
//
//        User expectedUser = new User();
//        expectedUser.setName(name);
//        expectedUser.setLogin(login);
//        expectedUser.setPassword(password);
//        expectedUser.setRole(role);
//        expectedUser.setSurname(surname);
//        expectedUser.setId(1);
//
//        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(birthDate);
//
//        when(userDAO.getClientRole()).thenReturn(role);
//        when(userDAO.isUserExist(login)).thenReturn(true);
//        when(userDAO.isUserExist(name, surname, date)).thenReturn(true);
//        when(userDAO.getUser(login)).thenReturn(expectedUser);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//
//        userService.addUser(user);
//    }
//
//    /**
//     * Tests method UserService.getUser. Test passed when expected data equals to created.
//     * @throws Exception
//     */
//    @Test
//    public void testGetUser_Success() throws Exception {
//        String login = "bred";
//        String stringDate = "12.02.1980";
//        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(stringDate);
//        String name = "Bred";
//        String surname = "Pit";
//        String password = "123";
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//
//        User user = new User();
//        user.setId(1);
//        user.setRole(role);
//        user.setPassword(password);
//        user.setName(name);
//        user.setSurname(surname);
//        user.setBirthDate(date);
//        user.setLogin(login);
//
//        UserTO expectedUserTO = new UserTO();
//        expectedUserTO.setName(name);
//        expectedUserTO.setSurname(surname);
//        expectedUserTO.setPassword(password);
//        expectedUserTO.setLogin(login);
//        expectedUserTO.setBirthDate(stringDate);
//        expectedUserTO.setRole(role.getName());
//        when(userDAO.isUserExist(login)).thenReturn(true);
//        when(userDAO.getUser(login)).thenReturn(user);
//        UserTO userTO = userService.getUser(login);
//        Assert.assertEquals(expectedUserTO.getName(), userTO.getName());
//        Assert.assertEquals(expectedUserTO.getSurname(), userTO.getSurname());
//        Assert.assertEquals(expectedUserTO.getBirthDate(), userTO.getBirthDate());
//        Assert.assertEquals(expectedUserTO.getLogin(), userTO.getLogin());
//        Assert.assertEquals(expectedUserTO.getPassword(), userTO.getPassword());
//    }
//
//    /**
//     * Tests method UserService.getUser. Test passed when throws UserNotFoundException.
//     * @throws Exception
//     */
//    @Test(expected = UserNotFoundException.class)
//    public void testGetUser_UserNotFound() throws Exception {
//        String login = "null";
//        String stringDate = "12.02.1980";
//        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(stringDate);
//        String name = "Bred";
//        String surname = "Pit";
//        String password = "123";
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//
//        User user = new User();
//        user.setId(1);
//        user.setRole(role);
//        user.setPassword(password);
//        user.setName(name);
//        user.setSurname(surname);
//        user.setBirthDate(date);
//        user.setLogin(login);
//
//        when(userDAO.getUser(login)).thenThrow(PersistenceException.class);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        userService.getUser(login);
//    }
//}
