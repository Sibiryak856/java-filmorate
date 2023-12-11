package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MpaRating {

    private Integer id;
    private String name;

    public MpaRating(Integer id) {
        this.id = id;
    }

    public MpaRating(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
