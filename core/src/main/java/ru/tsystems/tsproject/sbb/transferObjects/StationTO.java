package ru.tsystems.tsproject.sbb.transferObjects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Represents client entity of Station.
 * @author Daria Nikiforova
 */
public class StationTO {

    @NotNull
    @Pattern(regexp = "[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]")
    @Size(min = 3, max = 100)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public StationTO() {
    }

    public StationTO(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
