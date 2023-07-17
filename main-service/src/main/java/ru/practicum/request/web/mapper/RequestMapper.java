package ru.practicum.request.web.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.db.model.Request;
import ru.practicum.request.web.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class RequestMapper {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto requestToParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(request.getId());
        participationRequestDto.setCreated(request.getCreated().format(formatter));
        participationRequestDto.setEvent(request.getEvent());
        participationRequestDto.setRequester(request.getRequester());
        participationRequestDto.setStatus(request.getStatus());
        return participationRequestDto;
    }

    public static Request participationRequestDtoToRequest(ParticipationRequestDto participationRequestDto) {
        Request request = new Request();
        request.setId(participationRequestDto.getId());
        request.setEvent(participationRequestDto.getEvent());
        request.setCreated(LocalDateTime.parse(participationRequestDto.getCreated(), formatter));
        request.setRequester(participationRequestDto.getRequester());
        request.setStatus(participationRequestDto.getStatus());
        return request;
    }


}
