package ru.yandex.practicum.filmorate.controller;

import java.util.List;

public interface Controller<T> {

    List<T> getAll();
    T get(Integer id);
    T create(T t);
    T update(T t);
}
