package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class UserTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserLogin() {
        final User user = User.builder()
                .id(1L)
                .email("u@y.ru")
                .login("log in")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        Assert.isTrue(validates.size() > 0);
        validates.stream().map(v -> v.getMessage())
                .forEach(System.out::println);
    }
}
