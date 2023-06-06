package ru.practicum.mapper;

import ru.practicum.dto.PostStaticDto;
import ru.practicum.model.PostStatic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {
    public static PostStatic PostStaticDtoToPostStatic(PostStaticDto postStaticDto) {
        PostStatic postStatic = new PostStatic();
        postStatic.setApp(postStaticDto.getApp());
        postStatic.setIp(postStaticDto.getIp());
        postStatic.setUri(postStaticDto.getUri());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime result = LocalDateTime.parse(postStaticDto.getTimestamp(), formatter);
        postStatic.setCreated(result);
        return postStatic;
    }
}
