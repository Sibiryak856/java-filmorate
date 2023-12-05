package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FriendshipStatus {
    APPROVED("Подтвержденная"),
    NOT_APPROVED("Неподтвержденная");

    private String value;
}
