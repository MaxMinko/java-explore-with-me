package ru.practicum.user.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserDto {

    int id;
    @NotNull
    @NotEmpty
    @NotBlank
    @Length(min = 2)
    @Length(max = 250)
    String name;
    @NotNull
    @NotEmpty
    @Email
    @NotBlank
    @Length(min = 6)
    @Length(max = 254)
    String email;

}
