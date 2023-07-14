package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.category.db.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.web.dto.CategoryDto;
import ru.practicum.event.db.model.Event;
import ru.practicum.event.db.model.EventStatus;
import ru.practicum.event.db.repository.EventRepository;
import ru.practicum.event.web.dto.*;
import ru.practicum.event.web.mapper.EventMapper;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.EventValidationException;
import ru.practicum.exception.RequestValidationException;
import ru.practicum.exception.TimeValidationException;
import ru.practicum.request.db.model.Request;
import ru.practicum.request.db.model.RequestStatus;
import ru.practicum.request.service.RequestService;
import ru.practicum.request.web.dto.ParticipationRequestDto;
import ru.practicum.request.web.mapper.RequestMapper;
import ru.practicum.user.service.UserService;
import ru.practicum.user.web.mapper.UserMapper;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RequestService requestService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final StatClient statClient;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            @Lazy RequestService requestService,
                            @Lazy CategoryService categoryService,
                            @Lazy UserService userService,
                            @Lazy StatClient statClient) {
        this.eventRepository = eventRepository;
        this.requestService = requestService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.statClient = statClient;
    }

    @Transactional
    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, int userId) {
        Event event = EventMapper.newEventDtoToEvent(newEventDto);
        event.getCategory().setName(categoryService.getCategory(newEventDto.getCategory()).getName());
        event.setUser(UserMapper.toUser(userService.getUser(List.of(userId), 0, 1).get(0)));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new TimeValidationException("Время начала события должно быть позже.");
        }
        return EventMapper.eventToEventFullDto(eventRepository
                .save(event));
    }

    @Override
    public List<EventShortDto> getUserEvents(int userId, int from, int size) {
        List<EventShortDto> eventShortDtos = eventRepository.findByUserId(userId, PageRequest.of(from, size))
                .stream().map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
        eventShortDtos.stream()
                .forEach(x -> x.setConfirmedRequests(requestService.findRequestForOneEvent(x.getId())));
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEvent(int userId, int eventId) {
        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(eventRepository
                .findByUserIdAndId(userId, eventId));
        eventFullDto.setConfirmedRequests(requestService.findRequestForOneEvent(eventId));
        return eventFullDto;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findByUserIdAndId(userId, eventId);
        if (event == null) {
            throw new EventNotFoundException("Событие для редактирования не найдено.");
        }
        if (event.getState().equals(EventStatus.CANCELED.toString()) || event.getState()
                .equals(EventStatus.PENDING.toString())) {
            if (updateEventUserRequest.getEventDate() != null) {
                if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), formatter)
                        .isBefore(LocalDateTime.now().plusHours(2))) {
                    throw new TimeValidationException("Время события должно быть позже.");
                }
            }
            if (updateEventUserRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventUserRequest.getAnnotation());
            }
            if (updateEventUserRequest.getCategory() != null) {
                Category category = new Category();
                CategoryDto categoryDto = categoryService.getCategory(updateEventUserRequest.getCategory());
                category.setName(category.getName());
                categoryDto.setId(category.getId());
                event.setCategory(category);
            }
            if (updateEventUserRequest.getDescription() != null) {
                event.setDescription(updateEventUserRequest.getDescription());
            }
            if (updateEventUserRequest.getLocation() != null) {
                event.setLon(updateEventUserRequest.getLocation().getLon());
                event.setLat(updateEventUserRequest.getLocation().getLat());
            }
            if (updateEventUserRequest.getPaid() != null) {
                event.setPaid(updateEventUserRequest.getPaid());
            }
            if (updateEventUserRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
            }
            if (updateEventUserRequest.getRequestModeration() != null) {
                event.setRequestModeration(updateEventUserRequest.getRequestModeration());
            }
            if (updateEventUserRequest.getTitle() != null) {
                event.setTitle(updateEventUserRequest.getTitle());
            }
            if (updateEventUserRequest.getStateAction() != null) {
                if (updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                    event.setState(EventStatus.PENDING.toString());
                } else if (updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                    event.setState(EventStatus.CANCELED.toString());
                }
            }
        } else {
            throw new EventValidationException("Редактиовать можно только отмененные события или в ожидании модерации");
        }

        return EventMapper.eventToEventFullDto(eventRepository
                .save(event));
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(int userId, int eventId) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventsWithFilter(String text, List<Integer> categories,
                                                   Boolean paid, String rangeStart,
                                                   String rangeEnd, Boolean onlyAvailable,
                                                   String sort, int from, int size) {
        if (rangeEnd != null && rangeStart != null) {
            if (LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
                throw new TimeValidationException("Время начала события не может быть позже конца события.");
            }
        }


        if (text == null && paid == null && rangeStart == null && rangeEnd == null) {
            return eventRepository.findByCategoryId(categories, PageRequest.of(from, size))
                    .stream().map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
        }

        if (text != null && paid != null && categories != null) {
            return eventRepository.getEvents(text, categories, paid, PageRequest.of(from, size)).stream()
                    .map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
        }

        if (sort.equals("EVENT_DATE")) {
            return eventRepository.getEventsWithFilterSortEventDate(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter),
                            PageRequest.of(from, size)).stream().map(EventMapper::eventToEventShortDto)
                    .collect(Collectors.toList());
        }
        if (sort.equals("VIEWS")) {
            return eventRepository.getEventsWithFilterSortViews(text, categories, paid,
                            LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter),
                            PageRequest.of(from, size)).stream().map(EventMapper::eventToEventShortDto)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public EventFullDto getEvent(int id) {
        Event event = eventRepository.getEvent(id);

        if (event == null || !event.getState().equals("PUBLISHED")) {
            throw new EventNotFoundException("Событие не найдено.");
        }
        Integer views = 0;
        try {
            views = statClient.stats("?start=2020-05-05%2000:00:00&end=2055-05-05%2000:00:00&uris=/events/" +
                    id + "&unique=true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            statClient.hit("/events/" + id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EventFullDto eventFullDto = EventMapper.eventToEventFullDto(event);
        eventFullDto.setViews(views);
        return eventFullDto;
    }

    @Override
    public Event getEventById(int eventId) {
        Event event = eventRepository.getEvent(eventId);
        if (event == null) {
            throw new EventNotFoundException("Событие не найдено.");
        }
        return event;
    }


    @Override
    public List<EventFullDto> getEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                        String rangeStart, String rangeEnd, int from, int size) {
        if (users == null && states == null && categories == null && rangeStart == null && rangeEnd == null) {
            return eventRepository.findAll(PageRequest.of(from, size)).stream()
                    .map(EventMapper::eventToEventFullDto).collect(Collectors.toList());
        }
        if (states == null) {
            states = new ArrayList<>();
            states.add(EventStatus.PUBLISHED.toString());
            states.add(EventStatus.CANCELED.toString());
            states.add(EventStatus.PENDING.toString());
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().format(formatter);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(5).format(formatter);
        }
        List<Event> eventList = eventRepository
                .getEvents(users, states, categories, LocalDateTime.parse(rangeStart, formatter),
                        LocalDateTime.parse(rangeEnd, formatter), PageRequest.of(from, size))
                .stream().collect(Collectors.toList());
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : eventList) {
            EventFullDto eventFullDto = EventMapper.eventToEventFullDto(event, requestService
                    .findRequestForOneEvent(event.getId()));
            Integer views = 0;
            try {
            views = statClient.stats("?start=2020-05-05%2000:00:00&end=2055-05-05%2000:00:00&uris=/events/" +
                        eventFullDto.getId() + "&unique=true");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                statClient.hit("/events");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            eventFullDto.setViews(views);
            eventFullDtos.add(eventFullDto);
        }
        return eventFullDtos;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие для обновления не найдено."));
        if (event.getState().equals(EventStatus.PUBLISHED.toString())
                && updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
            throw new EventValidationException("Событие уже опубликовано.");
        }
        if (event.getState().equals(EventStatus.CANCELED.toString())
                && updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
            throw new EventValidationException("Нельзя опубликовать уже отмененное событие.");
        }
        if (event.getState().equals(EventStatus.PUBLISHED.toString())
                && updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
            throw new EventValidationException("Нельзя отменить опубликованное событие");
        }

        if (updateEventAdminRequest.getCategory() != null) {
            Category category = new Category();
            CategoryDto categoryDto = categoryService.getCategory(updateEventAdminRequest.getCategory());
            category.setName(categoryDto.getName());
            category.setId(categoryDto.getId());
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter)
                    .isBefore(LocalDateTime.now().plusHours(2))) {
                throw new TimeValidationException("Время начала события должно быть позже.");
            }
            event.setEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (event.getState().equals(EventStatus.PENDING.toString())) {
                if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                    event.setState(EventStatus.PUBLISHED.toString());
                    event.setPublishedOn(LocalDateTime.now());
                } else if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                    event.setState(EventStatus.CANCELED.toString());
                }
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        return EventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(int userId, int eventId) {
        List<String> requests = eventRepository.getRequest(eventId);
        List<ParticipationRequestDto> participationRequestDtos = new ArrayList<>();
        for (String request : requests) {
            List<String> subList = List.of(request.split(","));
            ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
            participationRequestDto.setId(Integer.parseInt(subList.get(0)));
            participationRequestDto.setCreated(subList.get(1).substring(0, 19));
            participationRequestDto.setEvent(Integer.parseInt(subList.get(2)));
            participationRequestDto.setRequester(Integer.parseInt(subList.get(3)));
            participationRequestDto.setStatus(subList.get(4));
            participationRequestDtos.add(participationRequestDto);
        }
        return participationRequestDtos;
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequest(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                        int userId, int eventId) {
        Event event = eventRepository.getUserEvent(userId, eventId);
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();

        if (event == null) {
            throw new EventNotFoundException("Событие не найдено.");
        }
        List<ParticipationRequestDto> participationRequestDto = requestService
                .findRequest(new ArrayList<>(eventRequestStatusUpdateRequest.getRequestIds()));
        if (participationRequestDto.get(0).getStatus()
                .equals(RequestStatus.CONFIRMED.toString()) && eventRequestStatusUpdateRequest.getStatus()
                .equals(RequestStatus.REJECTED.toString())) {
            throw new RequestValidationException("Невозможно отменить уже принятую заявку.");
        }
        if (!eventRequestStatusUpdateRequest.getStatus().equals("REJECTED")) {
            if ((event.getParticipantLimit() - requestService
                    .findRequestForOneEvent(eventId)) >= eventRequestStatusUpdateRequest.getRequestIds().size()) {
                List<ParticipationRequestDto> participationRequestDtos = requestService
                        .findRequest(eventRequestStatusUpdateRequest.getRequestIds());
                for (ParticipationRequestDto x : participationRequestDtos) {
                    x.setStatus(eventRequestStatusUpdateRequest.getStatus());
                }
                List<Request> requests = participationRequestDtos.stream()
                        .map(RequestMapper::participationRequestDtoToRequest).collect(Collectors.toList());
                requestService.saveRequests(requests);
                eventRequestStatusUpdateResult.setConfirmedRequests(requestService.saveRequests(requests));
                eventRequestStatusUpdateResult.setRejectedRequests(requestService
                        .findRequest(eventRequestStatusUpdateRequest.getRequestIds()));
                return eventRequestStatusUpdateResult;

            } else {
                throw new RequestValidationException("Достигнуть лимит одобренныых заявок.");
            }
        }
        List<Request> requests = requestService.findRequest(eventRequestStatusUpdateRequest.getRequestIds())
                .stream().map(RequestMapper::participationRequestDtoToRequest).collect(Collectors.toList());
        for (Request request : requests) {
            request.setStatus(RequestStatus.REJECTED.toString());
        }
        eventRequestStatusUpdateResult.setRejectedRequests(requestService.saveRequests(requests));
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<EventShortDto> getAllEvents(List<Integer> eventsId) {
        return eventRepository.findAllById(eventsId).stream()
                .map(EventMapper::eventToEventShortDto).collect(Collectors.toList());
    }
}
