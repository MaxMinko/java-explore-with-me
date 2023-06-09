package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.PostStaticDto;
import ru.practicum.model.PostStatic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatMapper {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static PostStatic postStaticDtoToPostStatic(PostStaticDto postStaticDto) {
        PostStatic postStatic = new PostStatic();
        postStatic.setApp(postStaticDto.getApp());
        postStatic.setIp(postStaticDto.getIp());
        postStatic.setUri(postStaticDto.getUri());
        LocalDateTime result = LocalDateTime.parse(postStaticDto.getTimestamp(), formatter);
        postStatic.setCreated(result);
        return postStatic;
    }
}
