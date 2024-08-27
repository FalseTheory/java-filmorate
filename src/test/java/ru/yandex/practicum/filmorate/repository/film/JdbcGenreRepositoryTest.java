package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcGenreRepository")
class JdbcGenreRepositoryTest {

    private final JdbcGenreRepository genreRepository;

    static Genre getTestGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("action");
        return genre;
    }

    static List<Genre> getTestGenreList() {
        Genre genre = new Genre();
        genre.setId(2L);
        genre.setName("horror");
        return List.of(
                getTestGenre(),
                genre
        );
    }


    @Test
    @DisplayName(" жанр должен корректно возвращаться по их id")
    void should_correctly_return_genres_by_ids() {
        Optional<Genre> genreOpt = genreRepository.getById(1L);

        assertThat(genreOpt)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestGenre());
    }


    @Test
    @DisplayName("Список всех жанров должен корректно возвращаться")
    void should_correctly_return_genres_list() {
        List<Genre> genreList = genreRepository.getAll();

        assertThat(genreList)
                .usingRecursiveComparison()
                .isEqualTo(getTestGenreList());
    }
}