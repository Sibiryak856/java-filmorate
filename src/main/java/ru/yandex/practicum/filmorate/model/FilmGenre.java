package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilmGenre { //https://habr.com/ru/articles/267389/
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    private String value;

    public static FilmGenre parse(String value) {
        if (value != null) {
            for (FilmGenre genre : FilmGenre.values()) {
                if (value.equalsIgnoreCase(genre.value)) {
                    return genre;
                }
            }
        }
        return null;
    }
}
