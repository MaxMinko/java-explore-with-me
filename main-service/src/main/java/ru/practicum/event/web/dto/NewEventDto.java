package ru.practicum.event.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.db.model.Location;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NewEventDto {
    @NotEmpty
    @NotBlank
    @Length(max = 2000, min = 20)
    String annotation;
    @NotNull
    Integer category;
    @NotEmpty
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;
    @NotEmpty
    @NotBlank
    String eventDate;
    @NotNull
    Location location;
    Boolean paid = false;
    Integer participantLimit = 0;
    Boolean requestModeration = true;
    @NotEmpty
    @NotBlank
    @Length(min = 3, max = 120)
    String title;
}
