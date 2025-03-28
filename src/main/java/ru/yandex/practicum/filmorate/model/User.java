package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String name;
    @NotEmpty(message = "Логин не должен быть null или пуст")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелов")
    private String login;
    @Email
    @NotEmpty
    private String email;
    private LocalDate birthday;
}
