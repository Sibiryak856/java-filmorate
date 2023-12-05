package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MPA {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private String value;

    public static MPA parse(String value) {
        if (value != null) {
            for (MPA mpa : MPA.values()) {
                if (value.equalsIgnoreCase(mpa.value)) {
                    return mpa;
                }
            }
        }
        return null;
    }
}
