package ru.tsystems.tsproject.sbb.transferObjects;

/**
 * Created by apple on 29.11.14.
 */
public class ServiceTO {
    private int id;

    private String name;

    private double value;

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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        return false;
    }
}
