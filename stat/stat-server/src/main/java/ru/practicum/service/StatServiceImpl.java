package ru.practicum.service;


import ru.practicum.dto.PostStaticDto;
import ru.practicum.dto.PostStaticDtoForResponse;
import lombok.AllArgsConstructor;
import ru.practicum.mapper.StatMapper;
import org.springframework.stereotype.Service;
import ru.practicum.repository.PostStaticDao;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class StatServiceImpl implements StatService {
    PostStaticDao postStaticDao;
    StatRepository statRepository;

    @Override
    public List<PostStaticDtoForResponse> getStats(String start, String end, boolean unique, List<String> uris) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        List<PostStaticDtoForResponse> staticList = new ArrayList<>();
        if (unique == false) {
            if (!uris.isEmpty()) {
                for (String uri : uris) {
                    PostStaticDtoForResponse postStaticDtoForResponse = new PostStaticDtoForResponse();
                    postStaticDtoForResponse.setUri(uri);
                    postStaticDtoForResponse.setApp("ewm-main-server");
                    postStaticDtoForResponse.setHits(statRepository.findStatic(startDate, endDate, uri));
                    staticList.add(postStaticDtoForResponse);
                }
            } else {
                staticList = postStaticDao.findAllStatic(startDate, endDate);
            }
        } else {
            if (!uris.isEmpty()) {
                for (String uri : uris) {
                    PostStaticDtoForResponse postStaticDtoForResponse = new PostStaticDtoForResponse();
                    postStaticDtoForResponse.setUri(uri);
                    postStaticDtoForResponse.setApp("ewm-main-server");
                    postStaticDtoForResponse.setHits(statRepository.findUniqStatic(startDate, endDate, uri));
                    staticList.add(postStaticDtoForResponse);
                }
            } else {
                staticList = postStaticDao.findAllUniqStatic(startDate, endDate);
            }
        }
        return staticList.stream().sorted(Comparator.comparingInt(PostStaticDtoForResponse::getHits).reversed()).collect(Collectors.toList());
    }

    @Override
    public PostStaticDto addHit(PostStaticDto postStaticDto) {
        statRepository.save(StatMapper.postStaticDtoToPostStatic(postStaticDto));
        return postStaticDto;
    }
}
