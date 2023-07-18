package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.db.model.User;
import ru.practicum.user.db.repository.UserRepository;
import ru.practicum.user.web.dto.UserDto;
import ru.practicum.user.web.mapper.UserMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public List<UserDto> getUser(List<Integer> usersId, int from, int size) {
        if (usersId.isEmpty()) {
            return userRepository.findAll(PageRequest.of(from, size)).stream().map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIds(usersId, PageRequest.of(from, size)).stream().map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
    @Override
    public User getUser(int userId){
        return userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("Пользователь не найден."));
    }

}
