package ru.tsystems.tsproject.sbb;

/**
 * Created by apple on 08.10.14.
 */

import org.apache.log4j.Logger;
import ru.tsystems.tsproject.sbb.database.entity.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
        static EntityManagerFactory emf = Persistence.createEntityManagerFactory("manager1");
        static EntityManager em = emf.createEntityManager();
    private static final Logger LOGGER = Logger.getLogger(Main.class);

        public static void main(String[] a) throws Exception {
            LOGGER.info("eeeee");
            Role role=null;
            try {
                em.getTransaction().begin();
                role  = new Role();
                role.setName("admin");
                em.persist(role);

                role = new Role();
                role.setName("client");
                em.persist(role);
                em.getTransaction().commit();
            }
            catch (Exception ex) {
            }
            finally {
                if(em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }

           /*FactoryDAOImpl factory = new FactoryDAOImpl();
            try {
                StationDAO stationDAO = factory.getStationDAO();
                stationDAO.addStation("Udelnaya");
                stationDAO.addStation("Repino");
                stationDAO.addStation("Losevo");
                stationDAO.addStation("Zelenogorsk");
            }
            catch (Exception ex) {
            }

            UserDAO userDao = factory.getUserDAO();
            role = userDao.getClientRole();

            try {
                userDao.addUser("Alex", "Smith", new SimpleDateFormat("dd-mm-yyyy").parse("24-08-1970"), "alex", "12345", role);
                userDao.addUser("Jemmy", "King", new SimpleDateFormat("dd-mm-yyyy").parse("27-09-1975"), "Jemmy", "12345", role);
                userDao.addUser("Jemmy", "Vera", new SimpleDateFormat("dd-mm-yyyy").parse("27-09-1975"), "Wang", "1111", role);
            }
            catch (Exception ex) {
            }
            User user = new User("Alex", "Smith", new SimpleDateFormat("dd-mm-yyyy").parse("24-08-1970"), "alex", "12345", role);
            //String resRole = userDao.getUsersRole(user);
            //System.out.println(resRole);
            //boolean isTrue = userDao.isAdmin(user);
            //System.out.println(isTrue);

            //-------

            /*try {
                TrainDAO trainDAO = factory.getTrainDAO();
                trainDAO.addTrain(20);
                trainDAO.addTrain(30);
                trainDAO.addTrain(40);
                trainDAO.addTrain(20);
            }
            catch (Exception ex) {
                System.out.println(ex);
            }*/
            //trainDAO.addTrain()
/*            Station station = new Station();
            //station.setName("Udelnaya");
            //station.addTimetable(new Timetable());
            //em.persist(station);
           // User user = new User();
            //em.persist(user);
            Ticket ticket = new Ticket();
            //em.persist(ticket);
            TimetableRecord timetable = new TimetableRecord();
            //em.persist(timetable);
            Train train = new Train();
            //em.persist(train);
//            em.flush();

            //Query query = em.createQuery("SELECT e FROM Student e");
            //List<Student> list = (List<Student>) query.getResultList();
            //System.out.println(list);

            //query = em.createQuery("SELECT d FROM Department d");
            //List<Department> dList = (List<Department>) query.getResultList();
            //System.out.println(dList);
            em.getTransaction().commit();
           em.close();
           emf.close();

            //Helper.checkData();*/
            em.close();
            emf.close();
        }
    }
