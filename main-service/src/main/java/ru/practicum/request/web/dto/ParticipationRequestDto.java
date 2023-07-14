package ru.practicum.request.web.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ParticipationRequestDto {

    String created;
    int event;
    int id;
    int requester;
    String status;
}
