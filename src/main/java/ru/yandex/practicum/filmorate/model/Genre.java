package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {

    private Integer id;

    public Genre(Integer id) {
        this.id = id;
    }
}
