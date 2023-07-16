package ru.practicum.event.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.web.dto.EventFullDto;
import ru.practicum.event.web.dto.EventShortDto;

import java.util.List;

@RestController
@RequestMapping(value = "/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventService eventService;

    @GetMapping()
    public List<EventShortDto> getEventsWithFilter(@RequestParam(value = "text", required = false) String text,
                                  @RequestParam(value = "categories", required = false) List<Integer> categories,
                                  @RequestParam(value = "paid", required = false) Boolean paid,
                                  @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                  @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                  @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                  @RequestParam(value = "sort", required = false) String sort,
                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        return eventService.getEventsWithFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventWithFilter(@PathVariable(value = "id") int id) {
        return eventService.getEvent(id);
    }
}
