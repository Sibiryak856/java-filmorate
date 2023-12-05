package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UserFriend {
    private Long userId;
    private Long friendId;
    private FriendshipStatus status;
}
