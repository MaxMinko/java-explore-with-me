package ru.practicum.event.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.user.web.dto.UserShortDto;

@Data
@AllArgsConstructor
public class EventShortDto {
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String eventDate;
    int id;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Integer views;

    public EventShortDto() {

    }

}
