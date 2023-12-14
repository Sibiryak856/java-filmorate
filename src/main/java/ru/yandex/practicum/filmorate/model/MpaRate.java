package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MpaRate {

    private Integer id;
    private String name;

    public MpaRate(Integer id) {
        this.id = id;
    }

    public MpaRate(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
