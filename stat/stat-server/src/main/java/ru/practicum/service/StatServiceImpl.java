package ru.practicum.service;

import ru.practicum.dto.PostStaticDto;
import ru.practicum.dto.PostStaticDtoForResponse;
import lombok.AllArgsConstructor;
import ru.practicum.exception.TimeValidationException;
import ru.practicum.mapper.StatMapper;
import org.springframework.stereotype.Service;
import ru.practicum.repository.StatRepository;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class StatServiceImpl implements StatService {
    StatRepository statRepository;

    @Override
    public List<PostStaticDtoForResponse> getStats(String start, String end, boolean unique, List<String> uris) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        List<PostStaticDtoForResponse> staticList = new ArrayList<>();
        if(startDate.isAfter(endDate)){
            throw new TimeValidationException("Дата начал не может быть позже даты конца события.");
        }
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
                List<String> list = statRepository.findAllStatic(startDate, endDate);
                for (String stat : list) {
                    List<String> subStat = List.of(stat.split(","));
                    PostStaticDtoForResponse postStaticDtoForResponse = new PostStaticDtoForResponse();
                    postStaticDtoForResponse.setUri(subStat.get(2));
                    postStaticDtoForResponse.setApp(subStat.get(1));
                    postStaticDtoForResponse.setHits(Integer.parseInt(subStat.get(0)));
                    staticList.add(postStaticDtoForResponse);
                }
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
                List<String> list = statRepository.findAllUniqStatic(startDate, endDate);
                for (String stat : list) {
                    List<String> subStat = List.of(stat.split(","));
                    PostStaticDtoForResponse postStaticDtoForResponse = new PostStaticDtoForResponse();
                    postStaticDtoForResponse.setUri(subStat.get(2));
                    postStaticDtoForResponse.setApp(subStat.get(1));
                    postStaticDtoForResponse.setHits(Integer.parseInt(subStat.get(0)));
                    staticList.add(postStaticDtoForResponse);
                }
            }
        }
        return staticList.stream().sorted(Comparator.comparingInt(PostStaticDtoForResponse::getHits).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PostStaticDto addHit(PostStaticDto postStaticDto) {
        statRepository.save(StatMapper.postStaticDtoToPostStatic(postStaticDto));
        return postStaticDto;
    }
}
