package ru.practicum.event.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.event.db.model.Location;
import ru.practicum.user.web.dto.UserShortDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class EventFullDto {
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    int id;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    int participantLimit;
    String publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    int views;
    List<CommentDto>comments;
}
