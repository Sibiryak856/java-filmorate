package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public static Mpa parse(Integer id) {
        if (id != null) {
            for (Mpa mpa : Mpa.values()) {
                if (mpa.getId() == id) {
                    return mpa;
                }
            }
        }
        return null;
    }

    public static boolean checkMpaID(Integer id) {
        if (id != null) {
            for (Mpa mpa : Mpa.values()) {
                if (id == mpa.id) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Mpa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
