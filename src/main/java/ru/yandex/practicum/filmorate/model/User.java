package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults (level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    Long id;
    String name;
    @NotBlank(message = "Логин не должен быть null или пуст")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелов")
    String login;
    @Email
    @NotBlank
    String email;
    LocalDate birthday;
}
