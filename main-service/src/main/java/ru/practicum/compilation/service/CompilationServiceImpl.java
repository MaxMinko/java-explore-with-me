package ru.practicum.compilation.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.db.model.Compilation;
import ru.practicum.compilation.db.model.EventsAndCompilationsIds;
import ru.practicum.compilation.db.repository.CompilationRepository;
import ru.practicum.compilation.db.repository.EventsAndCompilationsIdsRepository;
import ru.practicum.compilation.web.dto.CompilationDto;
import ru.practicum.compilation.web.dto.NewCompilationDto;
import ru.practicum.compilation.web.dto.UpdateCompilationRequest;
import ru.practicum.compilation.web.mapper.CompilationMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.event.web.dto.EventShortDto;
import ru.practicum.exception.CompilationNotFoundException;


import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final EventsAndCompilationsIdsRepository eventsAndCompilationsIdsRepository;


    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  EventsAndCompilationsIdsRepository eventsAndCompilationsIdsRepository,
                                  @Lazy EventService eventService) {
        this.compilationRepository = compilationRepository;
        this.eventService = eventService;
        this.eventsAndCompilationsIdsRepository = eventsAndCompilationsIdsRepository;
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(CompilationMapper
                .newCompilationDtoToCompilation(newCompilationDto));
        List<Integer> eventsIds = newCompilationDto.getEvents();
        if (eventsIds.size() != 0) {
            List<EventsAndCompilationsIds> eventsAndCompilationsIdsList = new ArrayList<>();
            for (Integer id : eventsIds) {
                EventsAndCompilationsIds eventsAndCompilationsIds = new EventsAndCompilationsIds();
                eventsAndCompilationsIds.setCompilationId(compilation.getId());
                eventsAndCompilationsIds.setEventId(id);
                eventsAndCompilationsIdsList.add(eventsAndCompilationsIds);
            }
            eventsAndCompilationsIdsRepository.saveAll(eventsAndCompilationsIdsList);
        }
        List<EventShortDto> events = eventService.getAllEvents(eventsIds);
        return CompilationMapper.compilationToCompilationDto(compilation, events);
    }

    @Override
    @Transactional
    public CompilationDto deleteCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборки для удаления не найдены."));
        List<Integer> eventsId = compilationRepository.findCompilationEvents(compilation.getId());
        List<EventShortDto> events = eventService.getAllEvents(eventsId);
        compilationRepository.deleteById(compId);
        eventsAndCompilationsIdsRepository.deleteByCompilationId(compId);
        return CompilationMapper.compilationToCompilationDto(compilation, events);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка для редактироввания не найдена."));
        Compilation compilationForUpdate = CompilationMapper
                .updateCompilationRequestToCompilation(updateCompilationRequest, compId);
        if (compilationForUpdate.getTitle() == null) {
            compilationForUpdate.setTitle(compilation.getTitle());
        }
        if (compilationForUpdate.getPinned() == null) {
            compilationForUpdate.setPinned(compilation.getPinned());
        }
        List<EventShortDto> events = eventService.getAllEvents(updateCompilationRequest.getEvents());
        List<Integer> eventsIds = updateCompilationRequest.getEvents();
        if (eventsIds.size() != 0) {
            List<EventsAndCompilationsIds> eventsAndCompilationsIdsList = new ArrayList<>();
            for (Integer id : eventsIds) {
                EventsAndCompilationsIds eventsAndCompilationsIds = new EventsAndCompilationsIds();
                eventsAndCompilationsIds.setCompilationId(compilation.getId());
                eventsAndCompilationsIds.setEventId(id);
                eventsAndCompilationsIdsList.add(eventsAndCompilationsIds);
            }
            eventsAndCompilationsIdsRepository.saveAll(eventsAndCompilationsIdsList);
        }
        return CompilationMapper.compilationToCompilationDto(compilationRepository.save(compilationForUpdate), events);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from, size)).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size));
        }
        List<Integer> ids = compilations.stream().map(c -> c.getId()).collect(Collectors.toList());
        List<EventsAndCompilationsIds> eventsAndCompilationsIdsList = eventsAndCompilationsIdsRepository
                .findByCompilationIdIn(ids);
        List<Integer> idsCompilationWithEvents = eventsAndCompilationsIdsList.stream()
                .map(x -> x.getCompilationId()).collect(Collectors.toList());
        List<Integer> eventsIds = eventsAndCompilationsIdsList.stream().map(x -> x.getEventId())
                .collect(Collectors.toList());
        List<EventShortDto> events = eventService.getAllEvents(eventsIds);
        List<CompilationDto> compilationDtosList = compilations.stream()
                .map(CompilationMapper::compilationToCompilationDto1).collect(Collectors.toList());
        for (CompilationDto compilation : compilationDtosList) {
            if (idsCompilationWithEvents.contains(compilation.getId())) {
                List<EventsAndCompilationsIds> eventsAndCompilationsIds =eventsAndCompilationsIdsList.stream()
                        .filter(x -> x.getCompilationId() == compilation.getId())
                        .collect(Collectors.toList());
                List<Integer> eventsId = eventsAndCompilationsIds .stream().map(x -> x.getEventId())
                        .collect(Collectors.toList());
                List<EventShortDto> eventsForCompilation = new ArrayList<>();
                for (Integer id : eventsId) {
                    List<EventShortDto> eventForCompilation = events.stream().filter(x -> x.getId() == id)
                            .collect(Collectors.toList());
                    eventsForCompilation.add(eventForCompilation.get(0));
                }
                compilation.setEvents(eventsForCompilation);
            } else {
                compilation.setEvents(new ArrayList<>());
            }
        }
        return compilationDtosList;
    }

    @Override
    public CompilationDto getCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка не найдена"));
        List<Integer> eventsId = compilationRepository.findCompilationEvents(compId);
        List<EventShortDto> events = eventService.getAllEvents(eventsId);
        return CompilationMapper.compilationToCompilationDto(compilation, events);
    }
}
