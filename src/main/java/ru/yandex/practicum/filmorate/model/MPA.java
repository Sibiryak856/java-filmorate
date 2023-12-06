package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MPA {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private int id;
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

    public static boolean checkMpaID(Integer id) {
        if (id != null) {
            for (MPA mpa : MPA.values()) {
                if (id == mpa.id) {
                    return true;
                }
            }
        }
        return false;
    }
}
