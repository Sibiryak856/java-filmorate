CREATE TABLE IF NOT EXISTS GENRES (
    GENRE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    GENRE_NAME varchar(14)
);


CREATE TABLE IF NOT EXISTS MPA (
    MPA_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    MPA_NAME varchar(5)
);

CREATE TABLE IF NOT EXISTS USERS (
    USER_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL varchar (255) NOT NULL,
	LOGIN varchar (255) NOT NULL,
	USER_NAME varchar(255),
	BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS USER_FRIENDS (
    USER_ID INTEGER NOT NULL REFERENCES USERS(USER_ID),
    FRIEND_ID INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS (
    FILM_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FILM_NAME varchar(255),
    DESCRIPTION varchar(200),
    RELEASE_DATE date,
    DURATION INTEGER, 
    MPA_ID INTEGER NOT NULL REFERENCES MPA(MPA_ID)
);

CREATE TABLE IF NOT EXISTS FILM_LIKES (
    FILM_ID INTEGER NOT NULL REFERENCES FILMS(FILM_ID),
    USER_ID INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_GENRES (
    FILM_ID INTEGER NOT NULL REFERENCES FILMS(FILM_ID),
    GENRE_ID INTEGER NOT NULL UNIQUE REFERENCES GENRES(GENRE_ID)
);