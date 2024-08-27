package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@Import({JdbcUserRepository.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepository")
class JdbcUserRepositoryTest {
    private final JdbcUserRepository userRepository;
    public static final long TEST_USER_ID = 1L;

    static User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setBirthday(LocalDate.of(2000, 1, 20));
        user.setLogin("user");
        user.setName("name");
        user.setEmail("mail@mail.com");

        return user;
    }

    static User getTestCommonFriend() {
        User user = new User();
        user.setId(2L);
        user.setBirthday(LocalDate.of(2001, 1, 20));
        user.setLogin("user2");
        user.setName("name2");
        user.setEmail("gmail@mail.com");
        return user;
    }

    static List<User> getTestUserList() {

        return List.of(
                getTestUser(),
                getTestCommonFriend(),
                getTestOtherFriend()
        );
    }

    static User getTestOtherFriend() {
        User user = new User();
        user.setId(3L);
        user.setBirthday(LocalDate.of(2004, 1, 20));
        user.setLogin("user3");
        user.setName("name3");
        user.setEmail("umail@mail.com");
        return user;
    }

    static User getTestUserForPost() {
        User user = new User();
        user.setBirthday(LocalDate.of(2005, 1, 20));
        user.setLogin("user4");
        user.setName("name4");
        user.setEmail("google@mail.com");
        return user;
    }


    static List<User> getFirstUserTestFriendsList() {
        return List.of(
                getTestCommonFriend(),
                getTestOtherFriend()
        );
    }

    @Test
    @DisplayName("Пользователь должен корректно создаваться")
    void should_correctly_create_user() {
        userRepository.create(getTestUserForPost());

        User user = getTestUserForPost();
        user.setId(4L);

        assertThat(userRepository.get(4L))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("Пользователь должен корректно обновляться")
    void should_correctly_update_user() {
        User user = getTestUser();
        user.setLogin("testTestTEST");
        user.setName("Anna");
        user.setEmail("test@test.com");

        userRepository.update(user);

        assertThat(userRepository.get(TEST_USER_ID))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("Пользователь должен корректно возвращаться по существующему id")
    void should_return_user_by_id() {

        Optional<User> optionalUser = userRepository.get(1L);

        assertThat(optionalUser)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getTestUser());
    }

    @Test
    @DisplayName("Должен возвращаться список всех пользователей")
    void should_return_all_users_in_list() {
        List<User> userList = userRepository.getAll();

        assertThat(userList)
                .usingRecursiveComparison()
                .isEqualTo(getTestUserList());
    }

    @Test
    @DisplayName("Список друзей пользователя должен корректно возвращаться")
    void should_return_user_friends_list() {
        List<User> friendsList = userRepository.returnFriendsList(TEST_USER_ID);

        assertThat(friendsList)
                .usingRecursiveComparison()
                .isEqualTo(getFirstUserTestFriendsList());
    }

    @Test
    @DisplayName("Список общих друзей двух пользователей должен корректно возращаться")
    void should_return_two_users_common_friends_list() {
        List<User> commonFriendsList = userRepository.returnCommonFriends(TEST_USER_ID, 3L);

        assertThat(commonFriendsList)
                .usingRecursiveComparison()
                .isEqualTo(List.of(getTestCommonFriend()));
    }
}