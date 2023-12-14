package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User {

    private Long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", email);
        values.put("LOGIN", login);
        values.put("USER_NAME", name);
        values.put("BIRTHDAY", birthday);
        return values;
    }
}