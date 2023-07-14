package ru.practicum.compilation.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.db.model.Compilation;
import ru.practicum.compilation.db.repository.CompilationRepository;
import ru.practicum.compilation.web.dto.CompilationDto;
import ru.practicum.compilation.web.dto.NewCompilationDto;
import ru.practicum.compilation.web.dto.UpdateCompilationRequest;
import ru.practicum.compilation.web.mapper.CompilationMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.event.web.dto.EventShortDto;
import ru.practicum.exception.CompilationNotFoundException;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Autowired
    CompilationServiceImpl(CompilationRepository compilationRepository, @Lazy EventService eventService) {
        this.compilationRepository = compilationRepository;
        this.eventService = eventService;
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(CompilationMapper
                .newCompilationDtoToCompilation(newCompilationDto));
        List<Integer> eventsId = new ArrayList<>();
        for (int eventId : newCompilationDto.getEvents()) {
            compilationRepository.addEventsId(compilation.getId(), eventId);
            eventsId.add(eventId);
        }
        List<EventShortDto> events = eventService.getAllEvents(eventsId);
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
        compilationRepository.delete(compId);
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
        for (Integer eventId : updateCompilationRequest.getEvents()) {
            compilationRepository.addEventsId(compId, eventId);
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
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<Integer> eventsId = compilationRepository.findCompilationEvents(compilation.getId());
            List<EventShortDto> events = eventService.getAllEvents(eventsId);
            compilationDtos.add(CompilationMapper.compilationToCompilationDto(compilation, events));
        }
        return compilationDtos;
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
