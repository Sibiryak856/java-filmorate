package ru.yandex.practicum.filmorate.model;

public enum FilmGenre { //https://habr.com/ru/articles/267389/
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private Integer id;
    private String value;

    private FilmGenre(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public Integer getId() {
        return this.id;
    }

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
