package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Mpa {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private int id;
    private String name;

    public static String parseMpaId(Integer id) {
        for (Mpa mpa : Mpa.values()) {
            if (mpa.getId() == id) {
                return mpa.name;
            }
        }
        return null;
    }

    public static boolean checkMpaID(Integer id) {
        return Arrays.stream(Mpa.values()).anyMatch(mpa -> id == mpa.id);
    }
}
