package ru.practicum.event.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommentDto {
    int id;
    @NotEmpty()
    @NotBlank
    @Length(max = 1000)
    String text;
    String authorName;
    int eventId;
    LocalDateTime created;
    public CommentDto(){

    }
}
