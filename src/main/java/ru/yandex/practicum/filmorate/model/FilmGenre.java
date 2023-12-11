package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FilmGenre { //https://habr.com/ru/articles/267389/
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private Integer id;
    private String name;

    public static FilmGenre parse(Integer id) {
        if (id != null) {
            for (FilmGenre genre : FilmGenre.values()) {
                if (genre.getId() == id) {
                    return genre;
                }
            }
        }
        return null;
    }
}
