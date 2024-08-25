package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.repository.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import({JdbcFilmRepository.class, FilmExtractor.class, FilmRowMapper.class, JdbcGenreRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository")
class JdbcFilmRepositoryTest {

    private final JdbcFilmRepository filmRepository;
    private static long TEST_FILM_ID = 1L;

    static Film getTestFilm() {
        Film film = new Film();
        film.setId(TEST_FILM_ID);
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2012,1, 20));
        film.setDuration(10);
        film.setMpa(new MPA(1L, "PG"));
        film.setGenres(new LinkedHashSet<>(
                List.of(
                        new Genre(1L,"action"),
                        new Genre(2L, "horror")
                )
        ));
        return film;

    }
    static List<Film> getTestFilmsList() {
        Film film = new Film();
        film.setId(2L);
        film.setName("film2");
        film.setDescription("description2");
        film.setReleaseDate(LocalDate.of(2012,1, 21));
        film.setDuration(20);
        film.setMpa(new MPA(1L, "PG"));
        film.setGenres(new LinkedHashSet<>(
                List.of(
                        new Genre(1L,"action")
                )
        ));
        return List.of(
                getTestFilm(),
                film
        );
    }

    @Test
    @DisplayName("Фильм должен корректно создаваться")
    void should_correctly_create_film() {
    }

    @Test
    @DisplayName("Фильм должен корректно обновляться")
    void should_correctly_update_film() {
    }

    @Test
    @DisplayName("Список всех фильмов должен корректно возвращаться")
    void should_correctly_return_all_films() {
        List<Film> filmList = filmRepository.getAll();
        System.out.println(filmList);

        assertThat(filmList)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilmsList());
    }

    @Test
    @DisplayName("Фильм должен находиться по его id")
    void should_return_film_by_id() {
        Optional<Film> resOpt = filmRepository.get(TEST_FILM_ID);

        assertThat(resOpt)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm());
    }

    @Test
    @DisplayName("Лайк пользователя должен корректно добавляться")
    void should_correctly_add_user_like() {
    }

    @Test
    @DisplayName("Лайк пользователя должен корректно удаляться")
    void should_correctly_delete_user_like() {
    }

    @Test
    @DisplayName("Должен возвращаться список популярных фильмов")
    void should_return_most_popular_films_list() {
    }
}