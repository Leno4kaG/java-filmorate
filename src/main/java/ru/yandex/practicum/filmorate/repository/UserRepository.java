package ru.yandex.practicum.filmorate.repository;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepository {

    Map<Integer, User> users = new HashMap<>();

    private static int id = 0;

   public User save(User user){
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }
   public User update(User user){
        if(users.get(user.getId()) == null){
            throw new ValidationException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        return user;
    }
   public List<User> getUsers(){
        return new ArrayList<>(users.values());
    }
}
