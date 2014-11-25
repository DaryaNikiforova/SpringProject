package ru.tsystems.tsproject.sbb.database.entity;

import javax.persistence.*;

/**
 * Represents fees and charges for calculating ticket prices. Uniqueness defines by rates name.
 * @author Daria Nikiforova
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Rate {
    /**
     * Id for uniqueness each rate entry.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    /**
     * The rate name.
     */
    private String name;

    /**
     * Rate value.
     */
    private double value;

    /**
     * Indicates is this rate relates to train (express, speedy, etc.)
     */

    private boolean forTrain;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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


    public boolean isForTrain() {
        return forTrain;
    }

    public void setForTrain(boolean forTrain) {
        this.forTrain = forTrain;
    }

    public Rate() {}

    public Rate(int id) {
        this.id = id;
    }

}
