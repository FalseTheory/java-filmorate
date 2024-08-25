package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserExtractor implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        User user = null;
        while(rs.next()){
            if(user==null){
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
            }

        }


        return user;
    }
}
