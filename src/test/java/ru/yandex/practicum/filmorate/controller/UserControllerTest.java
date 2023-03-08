package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.controller.TestDate.*;

class UserControllerTest {
    private ValidateService validateService = new ValidateService();
    private InMemoryUserStorage userStorage = new InMemoryUserStorage();
    private UserService userService = new UserService(userStorage);
    private UserController userController = new UserController(userService);

    @Test
    void createUserIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(null));
        assertEquals(USER_NULL, exception.getMessage());
    }
    @Test
    void createUserEmailIsNotCorrect(){
        User user = new User();
        user.setEmail(" ");
        User user1 = new User();
        user1.setEmail("email.ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(EMAIL_ERROR, exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> userController.createUser(user1));
        assertEquals(EMAIL_ERROR, exception1.getMessage());
    }
    @Test
    void createUserLoginIsEmpty(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("");
        User user1 = new User();
        user1.setEmail("email@m.ru");
        user1.setLogin("email ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(LOGIN_ERROR, exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> userController.createUser(user1));
        assertEquals(LOGIN_ERROR, exception1.getMessage());
    }
    @Test
    void createUserNameIsEmpty(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("login");
        user.setName("");
        user.setBirthday(LocalDate.parse("1995-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        User user1 = userController.createUser(user);
        assertEquals(user.getLogin(), user1.getName());
    }
    @Test
    void createUserBirthdayInFuture(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusMonths(1));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(BIRTHDAY_ERROR, exception.getMessage());
    }

    @Test
    void updateUserIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(null));
        assertEquals(USER_NULL, exception.getMessage());
    }
    @Test
    void updateUserEmailIsNotCorrect(){
        User user = new User();
        user.setEmail(" ");
        User user1 = new User();
        user1.setEmail("email.ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(EMAIL_ERROR, exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> userController.createUser(user1));
        assertEquals(EMAIL_ERROR, exception1.getMessage());
    }
    @Test
    void updateUserLoginIsEmpty(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("");
        User user1 = new User();
        user1.setEmail("email@m.ru");
        user1.setLogin("email ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(LOGIN_ERROR, exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> userController.createUser(user1));
        assertEquals(LOGIN_ERROR, exception1.getMessage());
    }
    @Test
    void updateUserNameIsEmpty(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("login");
        user.setName("");
        user.setBirthday(LocalDate.parse("1995-12-10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        User user1 = userController.createUser(user);
        assertEquals(user.getLogin(), user1.getName());
    }
    @Test
    void updateUserBirthdayInFuture(){
        User user = new User();
        user.setEmail("email@m.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusMonths(1));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(BIRTHDAY_ERROR, exception.getMessage());
    }
    @Test
    void addFriend(){
        User user = userController.createUser(TestDate.addUser());
        User user1 = userController.createUser(TestDate.addUser());

        User userWithFriend = userController.addFriend(user.getId(), user1.getId());
        assertEquals(user1.getId(), userWithFriend.getFriends().get(0));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userController.addFriend(user.getId(),-1));
        assertEquals(String.format("Пользователь № %d не найден", user.getId()), exception.getMessage());
    }
    @Test
    void deleteFriend(){
        User user = userController.createUser(TestDate.addUser());
        User user1 = userController.createUser(TestDate.addUser());
        userController.addFriend(user.getId(), user1.getId());

        User userWithOutFriend = userController.deleteFriend(user.getId(), user1.getId());
       assertTrue(userWithOutFriend.getFriends().isEmpty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userController.deleteFriend(user.getId(),-1));
        assertEquals("Пользователь № 1 не найден", exception.getMessage());
    }
    @Test
    void getFriends(){
        User user = userController.createUser(TestDate.addUser());
        User user1 = userController.createUser(TestDate.addUser());
        userController.addFriend(user.getId(), user1.getId());

        List<User> friends = userController.getFriends(user.getId());
        assertEquals(user1, friends.get(0));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userController.getFriends(999));
        assertEquals("Пользователь № 999 не найден", exception.getMessage());
    }
    @Test
    void getCommonFriends(){
        User user = userController.createUser(TestDate.addUser());
        User user1 = userController.createUser(TestDate.addUser());
        User user2 = userController.createUser(TestDate.addUser());
        userController.addFriend(user.getId(), user1.getId());
        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user1.getId(), user2.getId());

        List<User> commonFriends = userController.getCommonFriends(user.getId(), user1.getId());
        assertEquals(user2, commonFriends.get(0));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userController.getCommonFriends(999, 0));
        assertEquals("Пользователь № 999 не найден", exception.getMessage());
    }
}