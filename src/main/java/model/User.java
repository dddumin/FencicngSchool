package model;

import java.sql.Date;
import java.util.Objects;

public class User {
    private int id;
    private String login;
    private String password;
    private String name;
    private Date regDate;

    public User() {
    }

    public User(String login, String password, String name, Date regDate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.regDate = regDate;
    }

    public User(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public User(int id, String login, String password, String name, Date regDate) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.regDate = regDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(regDate, user.regDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, name, regDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
