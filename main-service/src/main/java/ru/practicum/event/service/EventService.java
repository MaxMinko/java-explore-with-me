package ru.practicum.event.service;

import ru.practicum.event.db.model.Event;
import ru.practicum.event.web.dto.*;
import ru.practicum.request.web.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    EventFullDto addEvent(NewEventDto newEventDto, int userId);

    List<EventShortDto> getUserEvents(int userId, int from, int size);

    EventFullDto getEvent(int userId, int eventId);

    EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getUserEventRequests(int userId, int eventId);


    List<EventShortDto> getEventsWithFilter(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable,
                                            String sort, int from, int size);

    EventFullDto getEvent(int id);


    Event getEventById(int eventId);

    List<EventFullDto> getEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart,
                                 String rangeEnd, int from, int size);

    EventFullDto updateEvent(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<ParticipationRequestDto> getRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequest(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                 int userId, int eventId);

    List<EventShortDto> getAllEvents(List<Integer> eventsId);
    CommentDto addComment(CommentDto commentDto, int userId, int itemId);

    void deleteComment(int eventId, int commentId);

    CommentDto updateComment(CommentDto commentDto, int userId, int eventId,int commentId);
}

