package ru.practicum.event.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.web.dto.*;
import ru.practicum.request.web.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(value = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;


    @PostMapping()
    public ResponseEntity<EventFullDto> addEvent(@Validated @RequestBody NewEventDto newEventDto,
                                                 @PathVariable("userId") int userId) {
        return new ResponseEntity<>(eventService.addEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<EventShortDto> getUserEvents(@PathVariable("userId") int userId,
                                             @RequestParam(value = "from", defaultValue = "0") int from,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {

        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId,
                                    @Validated @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable("userId") int userId,
                                                     @PathVariable("eventId") int eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@RequestBody EventRequestStatusUpdateRequest
                                                                eventRequestStatusUpdateRequest,
                                                        @PathVariable("userId") int userId,
                                                        @PathVariable("eventId") int eventId) {
        return eventService.updateRequest(eventRequestStatusUpdateRequest, userId, eventId);
    }

    @PostMapping("/{eventId}/comment")
    public ResponseEntity<CommentDto> addComment(@Validated @RequestBody CommentDto commentDto,
                                                 @PathVariable("userId") int userId,
                                                 @PathVariable("eventId") int eventId) {
        return new ResponseEntity<>(eventService.addComment(commentDto, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}/comment/{commentId}")
    public CommentDto updateComment(@Validated @RequestBody CommentDto commentDto,
                                    @PathVariable("userId") int userId,
                                    @PathVariable("eventId") int eventId,
                                    @PathVariable("commentId") int commentId) {
        return eventService.updateComment(commentDto, userId, eventId, commentId);
    }
}
