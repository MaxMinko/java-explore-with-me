package ru.practicum.compilation.web.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.db.model.Compilation;
import ru.practicum.compilation.web.dto.CompilationDto;
import ru.practicum.compilation.web.dto.NewCompilationDto;
import ru.practicum.compilation.web.dto.UpdateCompilationRequest;
import ru.practicum.event.web.dto.EventShortDto;

import java.util.List;

@UtilityClass
public class CompilationMapper {


    public static Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto compilationToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setEvents(events);
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
    public static CompilationDto compilationToCompilationDto1(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation updateCompilationRequestToCompilation(UpdateCompilationRequest updateCompilationRequest,
                                                                    int id) {
        Compilation compilation = new Compilation();
        compilation.setId(id);
        compilation.setTitle(updateCompilationRequest.getTitle());
        compilation.setPinned(updateCompilationRequest.getPinned());
        return compilation;
    }
}
