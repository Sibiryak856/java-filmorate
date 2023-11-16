package ru.yandex.practicum.filmorate;

import java.util.Set;

public class Constants {
    public static final String LIKE_CANCELLATION = "dislike";
    public static final String LIKE_ADDITION = "like";

    public static final Set<String> LIKE_ACTIONS = Set.of(LIKE_CANCELLATION, LIKE_ADDITION);

    public static final String FRIEND_ADDICTION = "add";
    public static final String FRIEND_CANCELLATION = "delete";

    public static final Set<String> FRIEDNS_LIST_ACTIONS = Set.of(FRIEND_ADDICTION, FRIEND_CANCELLATION);
}
