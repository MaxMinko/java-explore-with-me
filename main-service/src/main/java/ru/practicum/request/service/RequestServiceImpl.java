package ru.practicum.request.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.event.db.model.Event;
import ru.practicum.event.db.model.EventStatus;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.*;
import ru.practicum.request.db.model.Request;
import ru.practicum.request.db.model.RequestStatus;
import ru.practicum.request.db.repository.RequestRepository;
import ru.practicum.request.web.dto.ParticipationRequestDto;
import ru.practicum.request.web.mapper.RequestMapper;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventService eventService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, @Lazy EventService eventService) {
        this.requestRepository = requestRepository;
        this.eventService = eventService;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(int userId) {
        List<Request> requests = requestRepository.findByUserId(userId);
        return requests.stream().map(RequestMapper::requestToParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto addRequest(int userId, int eventId) {
        Request request = requestRepository.getRequestForAdd(userId, eventId);
        if (request != null) {
            throw new RepeatRequestException("Нелья отправлять заявку повторно.");
        }
        Request requestForAdd = new Request();
        Event event = eventService.getEventById(eventId);
        if (eventId != 0) {
            if (event.getUser().getId() == userId) {
                throw new EventValidationException("Инициатор события не может " +
                        "добавлять запрос на участие в своем событии.");
            }
            if (!event.getState().equals(EventStatus.PUBLISHED.toString())) {
                throw new EventValidationException("Событие должно быть опубликовано.");
            }
            if (event.getParticipantLimit() != 0 && event.getParticipantLimit() - findRequestForOneEvent(eventId) < 1) {
                throw new EventValidationException("Достигнут лимит запросов на участие.");
            }

            if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
                requestForAdd.setStatus(RequestStatus.PENDING.toString());
            } else {
                requestForAdd.setStatus(RequestStatus.CONFIRMED.toString());
            }
        } else {
            requestForAdd.setStatus(RequestStatus.PENDING.toString());
        }
        requestForAdd.setEvent(eventId);
        requestForAdd.setRequester(userId);
        requestForAdd.setCreated(LocalDateTime.now());
        Request requestForResponse = requestRepository.save(requestForAdd);
        return RequestMapper.requestToParticipationRequestDto(requestForResponse);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        Request request = requestRepository.getRequest(userId, requestId);
        if (request == null) throw new RequestNotFoundException("Заявка для удаления не найдена.");
        requestRepository.deleteById(requestId);
        request.setStatus(RequestStatus.CANCELED.toString());
        return RequestMapper.requestToParticipationRequestDto(request);
    }

    @Override
    public Integer findRequestForOneEvent(int eventId) {
        return requestRepository.findRequestForOneEvent(eventId);
    }
    @Override
    public Map<Integer, Integer> findRequestForEvents(List<Integer>eventsIds){
    List<Request>requests=  requestRepository.findRequestForEvents(eventsIds);
    Map<Integer, Integer>requestsStats=new HashMap<>();
    for(Request request:requests){
        if(!requestsStats.containsKey(request.getEvent())){
            requestsStats.put(request.getEvent(),1);
        }else {
          requestsStats.put(request.getEvent(),requestsStats.get(request.getEvent())+1);
        }
    }
     return requestsStats;
    }

    @Override
    public List<ParticipationRequestDto> findRequest(List<Integer> requestIds) {
        return requestRepository.findRequests(requestIds).stream().map(RequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> saveRequests(List<Request> requests) {
        return requestRepository.saveAll(requests).stream().map(RequestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
