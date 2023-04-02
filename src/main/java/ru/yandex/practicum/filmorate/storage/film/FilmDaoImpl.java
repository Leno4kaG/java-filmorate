package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Optional<Film> save(Film film) {
        final String INSERT_SQL = "insert into film (name, description, release_date, duration, mpa) values(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        Set<Genre> genres = updateGenresAndRating(film, film.getMpa().getId());
        film.updateGenre(genres);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", film.getId());
        if (filmRows.next()) {
            int mpaId = film.getMpa().getId();
            jdbcTemplate.update("UPDATE film set name = ?, description = ?, release_date = ?, duration = ?, mpa = ? where id = ?",
                    film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), mpaId, film.getId());

            log.info("Update film with id {}!!!", film.getId());

            deleteAllFilmInGenre(film.getId());
            Set<Genre> genres = updateGenresAndRating(film, mpaId);
            if (genres.isEmpty()) {
                for (Integer id : getGenreByFilmId(film.getId())) {
                    deleteFilmInGenre(film.getId(), id);
                }
                log.info("Delete film in genres table!!!");
            } else {
                film.updateGenre(genres);
            }
            return Optional.of(film);
        }

        return Optional.empty();
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getFilm(rs));
    }

    @Override
    public Optional<Film> getFilm(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", id);
        if (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("id"));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(filmRows.getDate("release_date").toLocalDate());
            film.setDuration(filmRows.getBigDecimal("duration").longValue());
            getLikes(film);
            film.setMpa(getRating(filmRows.getInt("mpa")).orElse(new Rating()));
            updateGenres(film);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        }
        log.info("фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    @Override
    public List<Film> getListFilms(int count) {
        String sql = "select f.* from film f LEFT JOIN likes l ON f.id = l.film_id GROUP BY f.id ORDER BY COUNT(l.USER_ID) DESC LIMIT " + count;
        return jdbcTemplate.query(sql, (rs, rowNum) -> getFilm(rs));
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO likes VALUES(?, ?)",
                userId, filmId);
        getFilm(filmId).orElseThrow().addLike(userId);
        return true;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?",
                userId, filmId);
        getFilm(filmId).orElseThrow().deleteLike(userId);
        return true;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getGenre(rs));
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        log.info("GET GENRE ID = {}", id);
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("id"));
            genre.setName(genreRows.getString("name"));
            return Optional.of(genre);
        }
        return Optional.empty();
    }

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT * FROM RATING";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getRating(rs));
    }

    @Override
    public Optional<Rating> getRating(int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select * from rating where id = ?", id);
        if (rowSet.next()) {
            Rating rating = new Rating();
            rating.setId(rowSet.getInt("id"));
            rating.setName(rowSet.getString("name"));
            return Optional.of(rating);
        }
        return Optional.empty();
    }

    private Film getFilm(ResultSet resultSet) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getBigDecimal("duration").longValue());
        getLikes(film);
        film.setMpa(getRating(resultSet.getInt("mpa")).orElse(new Rating()));
        updateGenres(film);
        return film;
    }

    private void getLikes(Film film) {
        String sql = String.format("select user_id from likes where film_id = %d", film.getId());
        List<Integer> likes = jdbcTemplate.query(sql, ((rs, rowNum) -> rs.getInt("user_id")));
        for (Integer like : likes) {
            film.addLike(like);
        }
    }

    private Genre getGenre(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }

    private Rating getRating(ResultSet resultSet) throws SQLException {
        Rating rating = new Rating();
        rating.setId(resultSet.getInt("id"));
        rating.setName(resultSet.getString("name"));
        return rating;
    }

    private void addGenreInFilm(int filmId, int genreId) {
        jdbcTemplate.update("INSERT INTO genres VALUES(?, ?)",
                filmId, genreId);
    }

    private List<Integer> getGenreByFilmId(int filmId) {
        String sql = String.format("select genre_id from genres where film_id = %d", filmId);
        return jdbcTemplate.query(sql, ((rs, rowNum) -> rs.getInt("genre_id")));
    }

    private void deleteFilmInGenre(int filmId, int genreId) {
        jdbcTemplate.update("DELETE FROM genres WHERE film_id = ? AND genre_id = ?",
                filmId, genreId);
    }

    private void deleteAllFilmInGenre(int filmId) {
        jdbcTemplate.update("DELETE FROM genres WHERE film_id = ?",
                filmId);
    }

    private Set<Genre> updateGenresAndRating(Film film, int mpaId) {
        Rating mpa = getRating(mpaId).orElseThrow(() -> new RatingNotFoundException("Рейтинг не найден!!"));
        film.setMpa(mpa);
        Set<Genre> genres = new HashSet<>();
        for (Genre genre : film.getGenres()) {
            if (genre != null) {
                addGenreInFilm(film.getId(), genre.getId());
                Genre genre1 = getGenre(genre.getId()).orElseThrow(() -> new GenreNotFoundException("Жанр не найден!!"));
                genres.add(genre1);
            }
        }
        return genres;
    }

    private void updateGenres(Film film) {
        Set<Genre> genres = new HashSet<>();
        for (Integer genreId : getGenreByFilmId(film.getId())) {
            if (genreId != null) {
                Genre genre1 = getGenre(genreId).orElseThrow(() -> new GenreNotFoundException("Жанр не найден!!"));
                genres.add(genre1);
            }
        }
        film.updateGenre(genres);
    }
}
