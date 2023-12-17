package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.CorrectLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {

    private Long id;
    @Email
    private String email;
    @NotBlank
    @CorrectLogin
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}