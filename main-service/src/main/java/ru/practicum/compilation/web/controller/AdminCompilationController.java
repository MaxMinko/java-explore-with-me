package ru.practicum.compilation.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.web.dto.CompilationDto;
import ru.practicum.compilation.web.dto.NewCompilationDto;
import ru.practicum.compilation.web.dto.UpdateCompilationRequest;

@RestController
@RequestMapping(value = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping()
    public ResponseEntity<CompilationDto> addCompilation(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.addCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<CompilationDto> deleteCompilation(@PathVariable("compId") int compId) {
        return new ResponseEntity<>(compilationService.deleteCompilation(compId), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") int compId,
                                     @Validated @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilation(compId, updateCompilationRequest);

    }
}
