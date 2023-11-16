package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.*;

@Component("memoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final ValidateService validateService;

    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Autowired
    public InMemoryUserStorage(ValidateService validateService) {
        this.validateService = validateService;
    }

    @Override
    public List<User> getAll() {
        log.info("Обработан запрос получения списка пользователей: {}", users);
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) {
        if (!users.containsKey(id)) {
            String error;
            if (id < 0) {
                error = String.format("Id пользователя не может быть отрицательным числом: %d", id);
                log.error(error);
                throw new NoSuchElementException(error);
            }
            error = String.format("Пользователя с таким id=%d не существует", id);
            log.error(error);
            throw new NoSuchElementException(error);
        }
        log.info("Обработан запрос получения фильма id {}", users.get(id));
        return users.get(id);
    }

    @Override
    public User create(User user) {
        validateService.userValidate(user);
        user.setId(++userId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Создан пользователь {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            String error = "Не указан id пользовавтеля";
            log.error(error);
            throw new ValidateException(error);
        }
        validateService.userValidate(user);
        if (!users.containsKey(user.getId())) {
            String error = String.format("Пользователя с таким id=%d не существует", user.getId());
            log.error(error);
            throw new NoSuchElementException(error);
        }
        users.put(user.getId(), user);
        log.info("Пользователь id {} обновлен: {}", user.getId(), user);
        return user;
    }
}
