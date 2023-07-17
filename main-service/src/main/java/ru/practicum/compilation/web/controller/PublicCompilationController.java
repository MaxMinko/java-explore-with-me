package ru.practicum.compilation.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.web.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping(value = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationDto> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") int compId) {
        return compilationService.getCompilation(compId);

    }
}
