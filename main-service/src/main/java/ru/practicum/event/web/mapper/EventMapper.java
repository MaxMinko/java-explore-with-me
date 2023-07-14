package ru.practicum.event.web.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.db.model.Category;
import ru.practicum.category.web.mapper.CategoryMapper;
import ru.practicum.event.db.model.Event;
import ru.practicum.event.db.model.EventStatus;
import ru.practicum.event.db.model.Location;
import ru.practicum.event.web.dto.*;
import ru.practicum.user.web.dto.UserShortDto;
import ru.practicum.user.web.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class EventMapper {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event newEventDtoToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        Category category = new Category();
        category.setId(newEventDto.getCategory());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setCreatedOn(LocalDateTime.now());
        event.setPublishedOn(null);
        event.setState(EventStatus.PENDING.toString());
        return event;
    }

    public static EventFullDto eventToEventFullDto(Event event, int confirmedRequest) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(confirmedRequest);
        eventFullDto.setCreatedOn(event.getCreatedOn().format(formatter));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(UserMapper.userToUserShortDto(event.getUser()));
        eventFullDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        } else {
            eventFullDto.setPublishedOn(null);
        }
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        return eventFullDto;
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setCreatedOn(event.getCreatedOn().format(formatter));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(new UserShortDto());
        eventFullDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        } else {
            eventFullDto.setPublishedOn(null);
        }
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setInitiator(UserMapper.userToUserShortDto(event.getUser()));
        return eventFullDto;
    }

    public static EventShortDto eventToEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setEventDate(event.getEventDate().format(formatter));
        eventShortDto.setInitiator(UserMapper.userToUserShortDto(event.getUser()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setId(event.getId());
        return eventShortDto;
    }

    public static Event updateEventUserRequestToEvent(UpdateEventUserRequest updateEventUserRequest,
                                                      int userId, int eventId) {
        Event event = new Event();
        event.setId(eventId);
        event.setAnnotation(updateEventUserRequest.getAnnotation());
        Category category = new Category();
        category.setId(updateEventUserRequest.getCategory());
        event.setCategory(category);
        event.setDescription(updateEventUserRequest.getDescription());
        event.setEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate(), formatter));
        event.setLon(updateEventUserRequest.getLocation().getLon());
        event.setLat(updateEventUserRequest.getLocation().getLat());
        event.setPaid(updateEventUserRequest.getPaid());
        event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        event.setTitle(updateEventUserRequest.getTitle());
        event.setPublishedOn(LocalDateTime.now());
        event.setCreatedOn(LocalDateTime.now());
        return event;
    }
}
