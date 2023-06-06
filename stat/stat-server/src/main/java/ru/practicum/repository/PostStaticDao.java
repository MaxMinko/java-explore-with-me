package ru.practicum.repository;

import ru.practicum.dto.PostStaticDtoForResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface PostStaticDao {

    List<PostStaticDtoForResponse> findAllStatic(LocalDateTime startDate, LocalDateTime endDate);

    List<PostStaticDtoForResponse> findAllUniqStatic(LocalDateTime startDate, LocalDateTime endDate);
}
