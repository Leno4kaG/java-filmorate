package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId) {
        User user = Optional.ofNullable(userStorage.getUserById(id)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        User otherUser = Optional.ofNullable(userStorage.getUserById(friendId)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        user.addFriend(friendId);
        otherUser.addFriend(id);
        log.info("Put user Id {} friend Id {}", id, friendId);
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = Optional.ofNullable(userStorage.getUserById(id)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        User otherUser = Optional.ofNullable(userStorage.getUserById(friendId)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        user.deleteFriend(friendId);
        otherUser.deleteFriend(id);
        log.info("DELETE user iD {} friends {}", id, friendId);
        return user;
    }

    public List<User> getFriends(int id) {
        User user = Optional.ofNullable(userStorage.getUserById(id)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));

        List<Integer> listId = user.getFriends();
       log.info("GET ID {} friends {}",id,  listId);
        return createFriendList(listId);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User user = Optional.ofNullable(userStorage.getUserById(id)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", id)));
        log.info("User {} by id {}", user, id);
        User otherUser = Optional.ofNullable(userStorage.getUserById(otherId)).orElseThrow(()->
                new UserNotFoundException(String.format("Пользователь № %d не найден", otherId)));
        log.info("Other User {} by id {}", otherUser, otherId);
        List<Integer> listId = user.getFriends();
        List<Integer> listOtherId = otherUser.getFriends();
        listId.retainAll(listOtherId);
        log.info("GET COMMON friends id {} other user ID {}", id, otherId);
        return createFriendList(listId);
    }

    private List<User> createFriendList(List<Integer> listId) {
        List<User> friends = new ArrayList<>();
        if(listId.isEmpty()){
            return friends;
        }
        for (Integer userId : listId) {
            User user = userStorage.getUserById(userId);
            if(user!=null) {
                friends.add(user);
            }
        }
        log.info("Friends {}", friends);
        return friends;
    }
}
