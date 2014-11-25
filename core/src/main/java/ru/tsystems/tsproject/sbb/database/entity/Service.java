package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Represents services for passengers. For each ticket fixed selected services.
 * @author Daria Nikiforova
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Service {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private double value;

    public Service() {}

    @ManyToMany(mappedBy="services", cascade = {CascadeType.ALL})
    private List<Ticket> tickets;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Service(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
