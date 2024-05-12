package ru.yandex.scooter.model.courier;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@JsonInclude(NON_NULL)
public class CourierData {
    private String login;
    private String password;
    private String firstname;

    public CourierData() {
    }

    public CourierData(String login, String password, String firstname) {
        this.login = login;
        this.password = password;
        this.firstname = firstname;
    }

    public static String generateRandomLogin() {
        return "testLogin_" + randomNumeric(3);
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
