package org.abzal1.dao.user;

import org.abzal1.dao.connection.JdbcConnection;
import org.abzal1.model.user.User;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao<User>{
    private final Connection connection;

    public UserDaoImpl(JdbcConnection jdbcConnection) {
        this.connection = jdbcConnection.getConnection();
    }

    @Override
    public void saveUser(User user) {
        String sql =
                "INSERT INTO " +
                        "users (name, creation_date) " +
                        "VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UserSQLQueries.INSERT_USER);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setDate(2, Date.valueOf(user.getStartDate()));
            //preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                user.setId(id);
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> fetchUserById(int id) {
        String sql =
                "SELECT * " +
                "FROM users " +
                "WHERE id = ?;";
        Optional<User> user = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UserSQLQueries.SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Date creationDate = resultSet.getDate("creation_date");

                user = Optional.of(new User(id, name, creationDate.toLocalDate()));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return user;
    }

    @Override
    public void deleteUserbyId(int id) {
        String sql =
                "DELETE FROM users " +
                        "WHERE user_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UserSQLQueries.DELETE_USER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void createAdminUser(User user) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UserSQLQueries.INSERT_ADMIN_USER);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDate(3, Date.valueOf(user.getStartDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
