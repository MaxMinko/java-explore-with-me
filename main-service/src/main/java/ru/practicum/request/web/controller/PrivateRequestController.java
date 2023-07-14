package ru.practicum.request.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.service.RequestService;
import ru.practicum.request.web.dto.ParticipationRequestDto;


import java.util.List;

@RestController
@RequestMapping(value = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final RequestService requestService;

    @GetMapping()
    List<ParticipationRequestDto> getRequests(@PathVariable("userId") int userId) {
        return requestService.getRequests(userId);
    }

    @PostMapping()
    ResponseEntity<ParticipationRequestDto> addRequest(@PathVariable("userId") int userId,
                                                       @RequestParam(value = "eventId") int eventId) {

        return new ResponseEntity<>(requestService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable("userId") int userId,
                                          @PathVariable("requestId") int requestId) {
        return requestService.cancelRequest(userId, requestId);
    }


}
