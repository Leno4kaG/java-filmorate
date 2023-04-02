package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.TestDate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDaoImpl;
import ru.yandex.practicum.filmorate.storage.user.UserDaoImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql(
        scripts = "classpath:film.sql"
)
class FilmorateApplicationTests {


    private final UserDaoImpl userDao;

    private final FilmDaoImpl filmDao;


    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userDao.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = filmDao.getFilm(2);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2)
                );
    }

    @Test
    public void testGetListUser() {

        List<User> userList = userDao.getUsers();

        assertThat(userList.size()).isEqualTo(4);
    }

    @Test
    public void testGetListFilm() {

        List<Film> filmList = filmDao.getFilms();

        assertThat(filmList.size()).isEqualTo(3);
    }

    @Test
    public void testCreateUser() {
        User user1 = TestDate.getUser();
        Optional<User> newUser = userDao.save(user1);
        assertThat(newUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", newUser.get().getId())
                );

    }

    @Test
    public void testCreateFilm() {
        Film film = TestDate.getFilm();
        film.setMpa(new Rating(1, null));
        Optional<Film> newFilm = filmDao.save(film);
        assertThat(newFilm)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("id", newFilm.get().getId())
                );
    }

    @Test
    public void testUpdateUser() {
        User user1 = userDao.getUser(1).get();
        user1.setLogin("newLogin");
        Optional<User> updateUser = userDao.update(user1);
        assertThat(updateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "newLogin")
                );
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmDao.getFilm(3).get();
        film.setMpa(new Rating(2, null));
        Optional<Film> updateFilm = filmDao.update(film);
        assertThat(updateFilm)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("mpa", new Rating(2, "Драма"))
                );
    }

    @Test
    public void testAddFriend() {
        userDao.addFriend(1, 2, true);
        assertThat(userDao.getFriends(1).get(0).getId()).isEqualTo(2);
    }

    @Test
    public void testDeleteFriend() {
        userDao.deleteFriend(1, 2);
        assertThat(userDao.getFriends(1).size()).isEqualTo(0);
    }

    @Test
    public void testGetCommonFriends() {
        userDao.addFriend(1, 4, true);
        List<User> commonFriends = userDao.getCommonFriends(4, 1);
        assertThat(commonFriends.get(0).getId()).isEqualTo(2);
    }

    @Test
    public void testGetListFilms() {
        List<Film> popularFilm = filmDao.getListFilms(3);
        assertThat(popularFilm.get(0).getId()).isEqualTo(2);
    }

    @Test
    public void testAddLike() {
        filmDao.addLike(1, 3);
        assertThat(filmDao.getFilm(1).get().getLikes().iterator().next()).isEqualTo(1);
    }

    @Test
    public void testDeleteLike() {
        filmDao.deleteLike(1, 3);
        assertThat(filmDao.getFilm(1).get().getLikes()).isNotIn(3);
    }

    @Test
    public void testGetGenre() {
        Optional<Genre> genre = filmDao.getGenre(1);
        assertThat(genre.get().getName()).isEqualTo("Комедия");
    }

    @Test
    public void testGetRating() {
        Optional<Rating> rating = filmDao.getRating(1);
        assertThat(rating.get().getName()).isEqualTo("G");
    }

    @Test
    public void testGetGenres() {
        List<Genre> genre = filmDao.getGenres();
        assertThat(genre.size()).isEqualTo(6);
    }

    @Test
    public void testGetRatings() {
        List<Rating> rating = filmDao.getRatings();
        assertThat(rating.size()).isEqualTo(5);
    }

}
