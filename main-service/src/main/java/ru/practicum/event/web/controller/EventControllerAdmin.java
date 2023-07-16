package ru.practicum.event.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.web.dto.EventFullDto;
import ru.practicum.event.web.dto.UpdateEventAdminRequest;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> getEvents(@RequestParam(value = "users", required = false) List<Integer> users,
                                 @RequestParam(value = "states", required = false) List<String> states,
                                 @RequestParam(value = "categories", required = false) List<Integer> categories,
                                 @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                 @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                 @RequestParam(value = "from", defaultValue = "0") int from,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable(value = "eventId", required = false) Integer eventId,
                             @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEvent(eventId, updateEventAdminRequest);

    }


}
