package ru.practicum.request.service;

import ru.practicum.request.db.model.Request;
import ru.practicum.request.web.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Map;

public interface RequestService {
    List<ParticipationRequestDto> getRequests(int userId);

    ParticipationRequestDto addRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    Integer findRequestForOneEvent(int eventId);
    Map<Integer,Integer> findRequestForEvents(List<Integer>eventsIds);

    List<ParticipationRequestDto> findRequest(List<Integer> requestIds);

    List<ParticipationRequestDto> saveRequests(List<Request> requests);
}
