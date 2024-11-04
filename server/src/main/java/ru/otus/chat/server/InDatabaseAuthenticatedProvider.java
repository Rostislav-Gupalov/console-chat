package ru.otus.chat.server;

import java.sql.SQLException;

    public class InDatabaseAuthenticatedProvider implements AuthenticatedProvider {

        private Server server;
        private UsersJdbc usersJdbc;

        public InDatabaseAuthenticatedProvider(Server server) throws SQLException, ClassNotFoundException {
            this.server = server;
            this.usersJdbc = new UsersJdbc();
        }

        @Override
        public void initialize() {
            System.out.println("Сервис аутентификации запущен: In memory режим");
        }

        private String getUsernameByLoginAndPassword(String login, String password) {
            for (User user : usersJdbc.getAll()) {
                if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                    return user.getUsername();
                }
            }
            return null;
        }

        @Override
        public synchronized boolean authenticate(ClientHandler clientHandler, String login, String password) {
            String authName = getUsernameByLoginAndPassword(login, password);
            if (authName == null) {
                clientHandler.sendMessage("Некорректный логин/пароль");
                return false;
            }
            if (server.isUsernameBusy(authName)) {
                clientHandler.sendMessage("Учетная запись уже занята");
                return false;
            }
            clientHandler.setUsername(authName);
            server.subscribe(clientHandler);
            clientHandler.sendMessage("/authok " + authName);
            return true;
        }
    }
//
