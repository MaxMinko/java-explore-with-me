package ru.practicum.event.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.db.model.Location;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UpdateEventAdminRequest {
    @Length(max = 2000, min = 20)
    String annotation;
    Integer category;
    @Length(min = 20, max = 7000)
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    @Length(min = 3, max = 120)
    String title;
}
