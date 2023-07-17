package ru.practicum.request.service;

import ru.practicum.request.db.model.Request;
import ru.practicum.request.web.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequests(int userId);

    ParticipationRequestDto addRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    Integer findRequestForOneEvent(int eventId);

    List<ParticipationRequestDto> findRequest(List<Integer> requestIds);

    List<ParticipationRequestDto> saveRequests(List<Request> requests);
}
