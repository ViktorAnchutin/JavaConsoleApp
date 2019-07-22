package model;

public class User {

    private int id;
    private String login;

    public User(String login)
    {
        this.login = login;
    }

    public User(){}

    public int getId() {return id;}

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    @Override
    public String toString() {
        return login ;
    }
}
