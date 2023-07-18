package ru.practicum.user.service;

import ru.practicum.user.db.model.User;
import ru.practicum.user.web.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getUser(List<Integer> usersId, int from, int size);

    void deleteUser(int userId);

    User getUser(int userId);

}
