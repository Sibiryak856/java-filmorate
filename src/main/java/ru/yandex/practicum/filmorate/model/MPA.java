package ru.yandex.practicum.filmorate.model;

public enum MPA {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

private Integer id;
    private String value;

    private MPA(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

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
