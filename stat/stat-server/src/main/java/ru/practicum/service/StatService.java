package ru.practicum.service;

import ru.practicum.dto.PostStaticDto;
import ru.practicum.dto.PostStaticDtoForResponse;

import java.util.List;

public interface StatService {
    List<PostStaticDtoForResponse> getStats(String start, String end, boolean unique, List<String> uris);

    PostStaticDto addHit(PostStaticDto postStaticDto);
}
