package ru.practicum.compilation.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UpdateCompilationRequest {

    List<Integer> events = new ArrayList<>();
    Boolean pinned;
    @Length(max = 50)
    String title;
}
