package ru.practicum.compilation.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.web.dto.EventShortDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CompilationDto {
    List<EventShortDto> events;
    int id;
    Boolean pinned;
    String title;

}
