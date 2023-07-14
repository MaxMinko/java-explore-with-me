package ru.practicum.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserShortDto {
    int id;
    String name;

    public UserShortDto() {

    }
}
