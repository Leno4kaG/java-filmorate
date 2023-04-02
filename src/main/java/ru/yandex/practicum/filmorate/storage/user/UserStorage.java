package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    public Optional<User> save(User user);

    public Optional<User> update(User user);

    public List<User> getUsers();

    public Optional<User> getUser(int id);

    boolean addFriend(int id, int friendId, boolean confirmation_status);

    boolean deleteFriend(int id, int friendId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int otherUserId);

}
