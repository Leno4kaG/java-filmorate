package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private ValidateService validateService = new ValidateService();
    private UserRepository userRepository = new UserRepository();
    private UserController userController = new UserController(validateService, userRepository);

    @Test
    void createUserIsNull() {
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(null));
        assertEquals("User не должен быть пустым", exception.getMessage());
    }
    @Test
    void createUserEmailIsNotCorrect(){
        User user = new User();
        user.setEmail(" ");
        User user1 = new User();
        user1.setEmail("email.ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Электронная почта не должна быть пустой", exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> userController.createUser(user1));
        assertEquals("Электронная почта не должна быть пустой", exception1.getMessage());
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
        assertEquals("Логин не должен быть пустым", exception.getMessage());
        ValidationException exception1 = assertThrows(ValidationException.class, () -> userController.createUser(user1));
        assertEquals("Логин не должен быть пустым", exception1.getMessage());
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
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }
}