package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(idToUser.values());
    }

    @Override
    public User create(User user) {
        validateName(user);
        validateDate(user);
        user.setId(idCounter++);
        idToUser.put(user.getId(), user);
        return user;
    }

    private void validateDate(User user) {
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday().isAfter(currentDate)) {
            String errorMessage = "Дата позже текущей";
            log.error(errorMessage);
            throw new InvalidBirthdayException(errorMessage);
        }
    }

    private void validateName(User user) {
        String newName = Optional.ofNullable(user.getName())
                .filter(name -> !name.isBlank())
                .orElse(user.getLogin());
        user.setName(newName);
    }

    @Override
    public User getById(Long id) {
        return idToUser.values().stream().filter(user -> Objects.equals(user.getId(), id)).findFirst().orElseThrow(
                () -> {
                    String errorMessage = String.format("Пользователь id %d не найден ", id);
                    log.error(errorMessage);
                    throw new UserNotFoundException(errorMessage);
                });
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (!idToUser.containsKey(id)) {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        if (idToUser.containsKey(id)) {
            idToUser.remove(id);
        } else {
            String errorMessage = String.format("Пользователь id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }
}
