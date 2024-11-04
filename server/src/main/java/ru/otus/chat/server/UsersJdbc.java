package ru.otus.chat.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsersJdbc implements UserService {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/jdbc";
    private static final String USERS_QUERY = "SELECT * from users_list";
    private Connection connection;

    public UsersJdbc() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL, "postgres", "postgres");
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            try(ResultSet resultSet = statement.executeQuery(USERS_QUERY)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String login = resultSet.getString(2);
                    String password = resultSet.getString(3);
                    String username = resultSet.getString(4);
                    User user = new User(id, login, password, username);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }


    @Override
    public void close() throws Exception {
    }
}
//