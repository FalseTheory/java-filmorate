package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@JdbcTest
@Import(JdbcMpaRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcMpaRepository")
class JdbcMpaRepositoryTest {

    private final JdbcMpaRepository mpaRepository;

    static MPA getTestMpa() {
        MPA mpa = new MPA(1L, "PG");
        return mpa;
    }

    @Test
    @DisplayName("Рейтинг фильма должен возвращаться по его id")
    void should_correctly_return_rating_by_id() {

        Optional<MPA> mpaOpt = mpaRepository.getById(1L);

        assertThat(mpaOpt)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestMpa());
    }
}