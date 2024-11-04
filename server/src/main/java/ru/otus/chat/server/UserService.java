package ru.otus.chat.server;

import java.util.List;

public interface UserService extends AutoCloseable{
    List<User> getAll();
}