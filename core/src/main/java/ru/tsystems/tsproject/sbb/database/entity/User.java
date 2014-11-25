package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Represents clients and employees. Client and employee have initially one role. Client can buy
 * tickets for different trips. Uniqueness defines by login and name, surname and birth date.
 * @author Nikiforova Daria
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"login"}),
                            @UniqueConstraint(columnNames = {"name", "surname", "birthDate"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    @Past
    private Date birthDate;

    private String login;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }



   public User(String name, String surname, Date birthDate, String login, String password, Role role) {
    this();
    this.name = name;
    this.surname = surname;
    this.birthDate = birthDate;
    this.login = login;
    this.password = password;
    this.role = role;
   }

   public User() {
    tickets = new ArrayList<Ticket>();
   }
}
