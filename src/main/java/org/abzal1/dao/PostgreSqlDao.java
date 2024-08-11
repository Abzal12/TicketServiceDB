package org.abzal1.dao;

import org.abzal1.dao.connection.JdbcConnection;
import org.abzal1.model.ticket.BusTicket;
import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class PostgreSqlDao implements Dao<BusTicket, User>{

    private final Connection connection;
    private static int counterForForeignUserId = 0;

    public PostgreSqlDao(JdbcConnection jdbcConnection) {
        this.connection = jdbcConnection.getConnection();
    }

    public void createTicketType() {
        final String sql =
                "CREATE TYPE ticket_type " +
                "AS ENUM ('DAY', 'WEEK', 'MONTH', 'YEAR');";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            System.out.println(e.getMessage());
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void createTables() {
        final String sql =
                "CREATE TABLE IF NOT EXISTS users (" +
                "user_id SERIAL PRIMARY KEY," +
                "name VARCHAR(255)," +
                "creation_date DATE" +
                ");" +
                "CREATE TABLE IF NOT EXISTS tickets (" +
                "ticket_id SERIAL," +
                "current_user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE," +
                "ticket_class VARCHAR(255)," +
                "ticket_type TICKET_TYPE," +
                "creation_date DATE," +
                "price DECIMAL" +
                ");";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            System.out.println(e.getMessage());
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void saveTicket(BusTicket busTicket) {
        String sql =
                "INSERT INTO " +
                "tickets (current_user_id, ticket_class, ticket_type, creation_date, price) " +
                "VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, counterForForeignUserId);
            preparedStatement.setString(2, busTicket.getTicketClass());
            preparedStatement.setObject(3, busTicket.getTicketType().name(), Types.OTHER);
            preparedStatement.setDate(4, Date.valueOf(busTicket.getStartDate()));
            preparedStatement.setBigDecimal(5, busTicket.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(User user) {
        String sql =
                "INSERT INTO " +
                "users (name, creation_date) " +
                "VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setDate(2, Date.valueOf(user.getStartDate()));
            counterForForeignUserId++;
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public Optional<BusTicket> fetchTicketById(int id) {
        String sql =
                "SELECT * " +
                "FROM tickets " +
                "WHERE ticket_id = ?;";
        Optional<BusTicket> busTicket = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String ticketClass = resultSet.getString("ticket_class");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                Date creationDate = resultSet.getDate("creation_date");
                BigDecimal price = resultSet.getBigDecimal("price");

                busTicket = Optional.of(new BusTicket(ticketClass, ticketType, creationDate.toLocalDate(), price));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return busTicket;
    }

    @Override
    public Optional<BusTicket> fetchTicketByUserId(int id) {
        String sql =
                "SELECT * " +
                "FROM tickets " +
                "WHERE current_user_id = ?;";
        Optional<BusTicket> busTicket = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String ticketClass = resultSet.getString("ticket_class");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                Date creationDate = resultSet.getDate("creation_date");
                BigDecimal price = resultSet.getBigDecimal("price");

                busTicket = Optional.of(new BusTicket(ticketClass, ticketType, creationDate.toLocalDate(), price));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return busTicket;
    }

    @Override
    public Optional<User> fetchUserById(int id) {
        String sql =
                "SELECT * " +
                "FROM users " +
                "WHERE user_id = ?;";
        Optional<User> user = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Date creationDate = resultSet.getDate("creation_date");

                user = Optional.of(new User(name, creationDate.toLocalDate()));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return user;
    }

    @Override
    public void updateTicketTypeById(int id, TicketType ticketType) {
        String sql =
                "UPDATE tickets " +
                "SET ticket_type = ? " +
                "WHERE ticket_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, ticketType.name(), Types.OTHER);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public void deleteUserbyId(int id) {
        String sql =
                "DELETE FROM users " +
                "WHERE user_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void saveTicketFromFile(BusTicket busTicket) {
        String sql =
                "INSERT INTO " +
                        "tickets (current_user_id, ticket_class, ticket_type, creation_date, price) " +
                        "VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, busTicket.getTicketClass());
            preparedStatement.setObject(3, busTicket.getTicketType().name(), Types.OTHER);
            preparedStatement.setDate(4, Date.valueOf(busTicket.getStartDate()));
            preparedStatement.setBigDecimal(5, busTicket.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void createAdminUser(User user) {
        String sql =
                "INSERT INTO " +
                        "users (user_id, name, creation_date) " +
                        "VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
