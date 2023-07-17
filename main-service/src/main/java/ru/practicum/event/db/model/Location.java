package ru.practicum.event.db.model;

import lombok.*;
import lombok.experimental.FieldDefaults;



@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class Location {

    Float lat;
    Float lon;

    public Location() {
    }
}
