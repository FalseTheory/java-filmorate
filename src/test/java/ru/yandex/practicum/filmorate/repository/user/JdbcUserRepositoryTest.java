package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import(JdbcUserRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepository")
class JdbcUserRepositoryTest {
    private final JdbcUserRepository userRepository;
    public static final long TEST_USER_ID = 1L;

    static User getTestUser() {
        User user = new User();

        return user;
    }

    @Test
    @DisplayName("Пользователь должен корректно создаваться")
    void should_correctly_create_user() {
    }

    @Test
    @DisplayName("Пользователь должен корректно обновляться")
    void should_correctly_update_user() {
    }

    @Test
    @DisplayName("Пользователь должен корректно возвращаться по существующему id")
    void should_return_user_by_id() {
    }

    @Test
    @DisplayName("Должен возвращаться список всех пользователей")
    void should_return_all_users_in_list() {
    }

    @Test
    @DisplayName("Добавление в друзья должен корректно обрабатываться")
    void should_correctly_add_friend_to_user() {
    }

    @Test
    @DisplayName("Удаление из друзей должно корректно обрабатываться")
    void should_correctly_delete_user_friend() {
    }

    @Test
    @DisplayName("Список друзей пользователя должен корректно возвращаться")
    void should_return_user_friends_list() {
    }

    @Test
    @DisplayName("Список общих друзей двух пользователей должен корректно возращаться")
    void should_return_two_users_common_friends_list() {
    }
}