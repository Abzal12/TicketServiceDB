package org.abzal1.dao.user;

import org.abzal1.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao<User>{
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveUser(User user) {
        jdbcTemplate.update(
                UserSQLQueries.INSERT_USER,
                user.getId(),
                user.getName(),
                Date.valueOf(user.getStartDate()));
    }

    @Override
    public Optional<User> fetchUserById(int id) {
        return jdbcTemplate.queryForObject(
                UserSQLQueries.SELECT_USER_BY_ID,
                (rs, rowNum) -> Optional.of(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("creation_date").toLocalDate()
                )),
                id
        );
    }

    @Override
    public void deleteUserbyId(int id) {
        jdbcTemplate.update(
                UserSQLQueries.DELETE_USER,
                id
        );
    }
}
