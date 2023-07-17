package ru.practicum.user.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.service.UserService;
import ru.practicum.user.web.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> addUser(@Validated @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<UserDto> getUser(@RequestParam(value = "ids", defaultValue = "") List<Integer> ids,
                          @RequestParam(value = "from", defaultValue = "0") int from,
                          @RequestParam(value = "size", defaultValue = "10") int size) {
        return userService.getUser(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
