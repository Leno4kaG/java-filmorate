package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final ValidateService validateService;
    private final UserRepository repository;

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        validateService.validateUser(newUser);
        User userSave = repository.save(newUser);
        log.info("Пользователь "+newUser.getName()+" добавлен");
        return userSave;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        validateService.validateUser(user);
        User userUpdate = repository.update(user);
        log.info("Пользователь с ID = "+user.getId()+" обновлен");
        return userUpdate;
    }

    @GetMapping
    public List<User> getUsers() {
        return repository.getUsers();
    }
}
