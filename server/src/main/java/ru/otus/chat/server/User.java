package ru.otus.chat.server;


public class User {
    private int id;
    private String login;
    private String password;
    private String username;

    public User(int id, String login, String password, String username) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getUsername() {
        return username;
    }

}
//