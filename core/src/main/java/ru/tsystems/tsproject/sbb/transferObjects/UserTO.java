package ru.tsystems.tsproject.sbb.transferObjects;

import ru.tsystems.tsproject.sbb.services.validators.AgeConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Represents client entity of User.
 * @author Daria Nikiforova
 */
public class UserTO {

    @Pattern(regexp = "[A-Za-z\\p{L}][A-Za-z-\\p{L}]{0,98}[A-Za-z\\p{L}]")
    @NotNull
    private String name;

    @Pattern(regexp = "[A-Za-z\\p{L}][A-Za-z-\\p{L}]{0,98}[A-Za-z\\p{L}]")
    @NotNull
    private String surname;

    @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4}")
    @NotNull
    @AgeConstraint
    private String birthDate;

    @Pattern(regexp = "[\\w]{1,100}")
    @NotNull
    @Size(min = 5, max = 100)
    private String login;

    @Pattern(regexp = "[\\w]{1,100}")
    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    private String role;

    public UserTO() {
        role = "client";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
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

}
