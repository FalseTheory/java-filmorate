package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(JdbcGenreRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcGenreRepository")
class JdbcGenreRepositoryTest {

    private final JdbcGenreRepository genreRepository;

    static Genre getTestGenre(){
        return new Genre(1L,"action");
    }
    static List<Genre> getTestGenreList(){
        return List.of(
                new Genre(1L,"action"),
                new Genre(2L, "horror")
        );
    }


    @Test
    @DisplayName("Список жанров должен корректно возвращаться по их id")
    void should_correctly_return_genres_by_ids() {
        Optional<Genre> genreOpt = genreRepository.getById(1L);

        assertThat(genreOpt)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestGenre());
    }

    @Test
    @DisplayName("Жанр по id фильма должен корректно возвращаться")
    void should_correctly_return_genre_by_id() {

        List<Genre> genreList = genreRepository.getByFilmId(1L);

        assertThat(genreList)
                .usingRecursiveComparison()
                .isEqualTo(getTestGenreList());
    }
}