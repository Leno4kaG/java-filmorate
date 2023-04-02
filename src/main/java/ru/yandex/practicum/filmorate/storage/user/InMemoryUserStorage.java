package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();

    private static int id = 0;

    @Override
    public Optional<User> save(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        if (users.get(user.getId()) == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(int id) {
        User user = users.get(id);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public boolean addFriend(int id, int friendId, boolean status) {
        User user = users.get(id);
        user.addFriend(friendId);
        log.info("User add {} {}", id, friendId);
        return true;
    }

    @Override
    public boolean deleteFriend(int id, int friendId) {
        User user = users.get(id);
        user.deleteFriend(friendId);
        log.info("User delete {} {}", id, friendId);
        return true;
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = getUser(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", userId)));

        List<Integer> listId = user.getFriends();
        return createFriendList(listId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = getUser(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", userId)));
        log.info("User {} by id {}", user, userId);
        User otherUser = getUser(otherId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь № %d не найден", otherId)));
        log.info("Other User {} by id {}", otherUser, otherId);
        List<Integer> listId = user.getFriends();
        List<Integer> listOtherId = otherUser.getFriends();
        listId.retainAll(listOtherId);
        log.info("GET COMMON friends id {} other user ID {}", userId, otherId);
        return createFriendList(listId);
    }

    private List<User> createFriendList(List<Integer> listId) {
        List<User> friends = new ArrayList<>();
        if (listId.isEmpty()) {
            return friends;
        }
        for (Integer userId : listId) {
            getUser(userId).ifPresent(friends::add);
        }
        return friends;
    }
}
