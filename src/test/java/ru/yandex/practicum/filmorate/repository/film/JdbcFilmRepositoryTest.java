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


@JdbcTest
@Import({JdbcFilmRepository.class, FilmExtractor.class, FilmRowMapper.class, JdbcGenreRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository")
class JdbcFilmRepositoryTest {

    private final JdbcFilmRepository filmRepository;
    private static long TEST_FILM_ID = 1L;

    static List<MPA> getMpaList() {
        MPA mpa1 = new MPA();
        mpa1.setId(1);
        mpa1.setName("PG");
        MPA mpa2 = new MPA();
        mpa2.setId(2);
        mpa2.setName("PG-13");
        return List.of(mpa1, mpa2);
    }

    static List<Genre> getGenresList() {
        Genre genre1 = new Genre();
        genre1.setId(1);
        genre1.setName("action");
        Genre genre2 = new Genre();
        genre2.setId(2);
        genre2.setName("horror");
        return List.of(genre1, genre2);
    }


    static Film createTestFilm() {
        Film film = new Film();
        film.setName("newFilm");
        film.setDescription("newDescription");
        film.setReleaseDate(LocalDate.of(2024, 1, 20));
        film.setDuration(30);
        film.setMpa(getMpaList().get(1));
        film.setGenres(new LinkedHashSet<>(
                List.of(
                        getGenresList().getFirst()
                )
        ));
        return film;
    }


    static Film getTestFilm() {
        Film film = new Film();
        film.setId(TEST_FILM_ID);
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2012, 1, 20));
        film.setDuration(10);
        film.setMpa(getMpaList().getFirst());
        film.setGenres(new LinkedHashSet<>(
                getGenresList()
        ));
        return film;

    }

    static List<Film> getTestFilmsList() {
        Film film = new Film();
        film.setId(2L);
        film.setName("film2");
        film.setDescription("description2");
        film.setReleaseDate(LocalDate.of(2012, 1, 21));
        film.setDuration(20);
        film.setMpa(getMpaList().getLast());
        film.setGenres(new LinkedHashSet<>(
                List.of(
                        getGenresList().getFirst()
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

        Film film = filmRepository.save(createTestFilm());

        Optional<Film> optionalFilm = filmRepository.get(film.getId());

        System.out.println(optionalFilm);


        assertThat(filmRepository.get(film.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("Фильм должен корректно обновляться")
    void should_correctly_update_film() {
        Film film = getTestFilm();
        film.setMpa(getMpaList().getLast());
        film.setGenres(new LinkedHashSet<>(List.of(getGenresList().getFirst())));
        film.setDuration(44);

        filmRepository.update(film);

        assertThat(filmRepository.get(film.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    @DisplayName("Список всех фильмов должен корректно возвращаться")
    void should_correctly_return_all_films() {
        List<Film> filmList = filmRepository.getAll();

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
    @DisplayName("Должен возвращаться список популярных фильмов")
    void should_return_most_popular_films_list() {
        List<Film> popularFilmList = filmRepository.getMostPopularFilms(2);

        assertThat(popularFilmList)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilmsList().reversed());

    }
}