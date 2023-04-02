package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> save(User user) {
        final String INSERT_SQL = "insert into user (email, login, name, birthday) values(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
               connection -> {
                   PreparedStatement ps =
                           connection.prepareStatement(INSERT_SQL, new String[] {"id"});
                   ps.setString(1, user.getEmail());
                   ps.setString(2, user.getLogin());
                   ps.setString(3, user.getName());
                   ps.setDate(4, Date.valueOf(user.getBirthday()));
                   return ps;
               },
                keyHolder);
       user.setId(keyHolder.getKey().intValue());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from user where id = ?", user.getId());
        if (userRows.next()) {
            jdbcTemplate.update("UPDATE user set email = ?, login = ?, name = ?, birthday = ? where id = ?",
                    user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            return Optional.of(user);
        }
            log.info("Пользователь с идентификатором {} не найден.", user.getId());
            return Optional.empty();

    }

    @Override
    public List<User> getUsers() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs));
    }

    @Override
    public Optional<User> getUser(int id) {
        User user = new User();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from user where id = ?", id);
        if (userRows.next()) {

            user.setId(userRows.getInt("id"));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());
            getFriendsToUser(user);
            return Optional.of(user);
        }
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
    }

    @Override
    public boolean deleteFriend(int id, int friendId) {
        jdbcTemplate.update("DELETE FROM friends where user_id = ? AND friend_id = ?",
                id, friendId);
        return true;
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = String.format("select * from user u where u.id in (select f.friend_id from friends f where f.user_id = %d)", userId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs));
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {

        String sql = String.format("SELECT * FROM USER u WHERE u.ID IN ((SELECT friend_id FROM FRIENDS f WHERE f.USER_ID = %d " +
                "AND f.FRIEND_ID in (SELECT friend_id FROM FRIENDS f2 WHERE f2.USER_ID = %d)))", userId, otherUserId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> getUser(rs));
    }
    @Override
    public boolean addFriend(int userId, int friendId, boolean confirmationStatus) {
        jdbcTemplate.update("INSERT INTO friends VALUES(?, ?, ?)",
                userId, friendId, confirmationStatus);
        return true;
    }

    private void getFriendsToUser(User user) {
        SqlRowSet friendsRow = jdbcTemplate.queryForRowSet("select * from friends where user_id = ?", user.getId());
        if (friendsRow.next()) {
            user.addFriend(friendsRow.getInt("friend_id"));
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        getFriendsToUser(user);
        return user;
    }
}
