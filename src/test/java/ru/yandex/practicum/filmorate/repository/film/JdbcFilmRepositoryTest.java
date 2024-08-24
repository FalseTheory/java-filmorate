package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import(JdbcFilmRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository")
class JdbcFilmRepositoryTest {

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
    }

    @Test
    @DisplayName("Фильм должен находиться по его id")
    void should_return_film_by_id() {
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