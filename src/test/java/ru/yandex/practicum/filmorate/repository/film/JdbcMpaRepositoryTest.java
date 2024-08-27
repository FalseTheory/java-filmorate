package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@JdbcTest
@Import(JdbcMpaRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcMpaRepository")
class JdbcMpaRepositoryTest {

    private final JdbcMpaRepository mpaRepository;

    static Mpa getTestMpa() {
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("PG");
        return mpa;
    }

    static List<Mpa> getTestMpaList() {
        Mpa mpa = new Mpa();
        mpa.setId(2L);
        mpa.setName("PG-13");
        return List.of(
                getTestMpa(),
                mpa
        );
    }

    @Test
    @DisplayName("Рейтинг фильма должен возвращаться по его id")
    void should_correctly_return_rating_by_id() {

        Optional<Mpa> mpaOpt = mpaRepository.getById(1L);

        assertThat(mpaOpt)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestMpa());
    }

    @Test
    @DisplayName("Список всех рейтингов должен корректно возвращаться")
    void should_correctly_return_all_ratings_list() {
        List<Mpa> mpas = mpaRepository.getAll();

        assertThat(mpas)
                .usingRecursiveComparison()
                .isEqualTo(getTestMpaList());
    }
}