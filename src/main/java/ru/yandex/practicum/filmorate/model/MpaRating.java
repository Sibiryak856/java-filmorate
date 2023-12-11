package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
