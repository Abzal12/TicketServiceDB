package org.abzal1.dao.user;

public class UserSQLQueries {
    public static final String INSERT_USER =
            "INSERT INTO " +
            "users (name, creation_date) " +
            "VALUES (?, ?) RETURNING id";
    public static final String INSERT_ADMIN_USER =
            "INSERT INTO " +
            "users (id, name, creation_date) " +
            "VALUES (?, ?, ?)";
    public static final String SELECT_USER_BY_ID =
            "SELECT * " +
            "FROM users " +
            "WHERE id = ?;";
    public static final String DELETE_USER =
            "DELETE FROM users " +
            "WHERE id = ?;";
}
