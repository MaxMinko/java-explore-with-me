package ru.practicum.compilation.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NewCompilationDto {
    List<Integer> events = new ArrayList<>();
    Boolean pinned = false;
    @NotEmpty
    @NotBlank
    @NotNull
    @Length(max = 50)
    String title;
}
