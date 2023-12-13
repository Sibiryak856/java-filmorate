package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Genres {

    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");



    private int id;
    private String name;

    public static String parseGenre(Integer id) {
        for (Genres genre : Genres.values()) {
            if (genre.getId() == id) {
                return genre.name;
            }
        }
        return null;
    }

    public static boolean checkGenreID(Integer id) {
        return Arrays.stream(Genres.values()).anyMatch(genre -> id == genre.id);
    }
}
