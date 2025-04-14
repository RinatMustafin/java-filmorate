package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {

    public List<User> getAll();

    public User create(User user);

    public User getById(Long id);

    public User update(User user);

    public void deleteById(Long id);
}
