package ru.practicum.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.dto.PostStaticDto;
import ru.practicum.dto.PostStaticDtoForResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("")
public class StatController {
    private final StatService statService;


    @PostMapping("/hit")
    public ResponseEntity<PostStaticDto> addHit(@RequestBody PostStaticDto postStaticDto) {
        return new ResponseEntity<>(statService.addHit(postStaticDto), HttpStatus.CREATED);
    }


    @GetMapping("/stats")
    public List<PostStaticDtoForResponse> getStats(@RequestParam(value = "start") String start,
                                                   @RequestParam(value = "end") String end,
                                                 @RequestParam(value = "unique", defaultValue = "false") boolean unique,
                                                 @RequestParam(value = "uris", defaultValue = "") List<String> uris) {
        return statService.getStats(start, end, unique, uris);
    }
}
