package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.net.http.HttpRequest;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PostStaticDto  {
    String app;
    String uri;
    String ip;
    String timestamp;
}
