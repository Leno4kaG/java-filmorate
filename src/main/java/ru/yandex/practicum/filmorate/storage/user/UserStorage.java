package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> save(User user);

    Optional<User> update(User user);

    List<User> getUsers();

    Optional<User> getUser(int id);

    boolean addFriend(int id, int friendId, boolean confirmationStatus);

    boolean deleteFriend(int id, int friendId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int otherUserId);

}
