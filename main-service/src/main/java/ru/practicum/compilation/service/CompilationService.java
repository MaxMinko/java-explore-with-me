package ru.practicum.compilation.service;

import ru.practicum.compilation.web.dto.CompilationDto;
import ru.practicum.compilation.web.dto.NewCompilationDto;
import ru.practicum.compilation.web.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto deleteCompilation(int compId);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(int compId);
}
