package ru.practicum.user.web.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.user.db.model.User;
import ru.practicum.user.web.dto.UserDto;
import ru.practicum.user.web.dto.UserShortDto;


@UtilityClass
public class UserMapper {

    public static User toUser(UserDto userDto){
        User user =new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }
    public static UserDto toUserDto(User user){
        UserDto userDto=new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto userToUserShortDto(User user){
        UserShortDto userShortDto=new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

}
