package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {


    private final UserStorage userStorage;


    public UserService(@Qualifier("userDaoImpl") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public User createUser(User newUser) {
        ValidateService.validateUser(newUser);
        User userSave = userStorage.save(newUser).orElseThrow(() -> new RuntimeException(String.format("При сохранении пользователя с id %d произошла ошибка!!", newUser.getId())));
        log.info("Пользователь добавлен {} ", newUser.getName());
        return userSave;
    }

    public User updateUser(User user) {
        ValidateService.validateUser(user);
        User userUpdate = userStorage.update(user).orElseThrow(() -> new UserNotFoundException(String.format("При обновлении пользователя с id %d произошла ошибка!!", user.getId())));
        log.info("Пользователь обновлен с ID = {} ", user.getId());
        return userUpdate;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUser(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
    }

    public User addFriend(int id, int friendId, boolean confirmation_status) {
        User user = userStorage.getUser(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        User otherUser = userStorage.getUser(friendId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        userStorage.addFriend(id, friendId, confirmation_status);
        log.info("Put user Id {} friend Id {}", id, friendId);
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getUser(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        User otherUser = userStorage.getUser(friendId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        userStorage.deleteFriend(id, friendId);
        log.info("DELETE user iD {} friends {}", id, friendId);
        return user;
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);

    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }


}
