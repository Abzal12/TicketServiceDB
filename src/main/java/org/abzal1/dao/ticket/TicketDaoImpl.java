package org.abzal1.dao.ticket;

import org.abzal1.dao.user.UserSQLQueries;
import org.abzal1.dao.connection.JdbcConnection;
import org.abzal1.model.ticket.BusTicket;
import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class TicketDaoImpl implements TicketDao<BusTicket> {

    private final Connection connection;

    public TicketDaoImpl(JdbcConnection jdbcConnection) {
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
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(255)," +
                "creation_date DATE" +
                ");" +
                "CREATE TABLE IF NOT EXISTS tickets (" +
                "id SERIAL," +
                "user_id INTEGER REFERENCES users (id) ON DELETE CASCADE," +
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
    public void saveTicket(BusTicket busTicket, int user_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.INSERT_TICKET);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, busTicket.getTicketClass());
            preparedStatement.setObject(3, busTicket.getTicketType().name(), Types.OTHER);
            preparedStatement.setDate(4, Date.valueOf(busTicket.getStartDate()));
            preparedStatement.setBigDecimal(5, busTicket.getPrice());
           // preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                busTicket.setId(id);
            }
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
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.SELECT_TICKET_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int ticket_id = resultSet.getInt("ticket_id");
                String ticketClass = resultSet.getString("ticket_class");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                Date creationDate = resultSet.getDate("creation_date");
                BigDecimal price = resultSet.getBigDecimal("price");

                busTicket = Optional.of(new BusTicket(id, ticket_id, ticketClass, ticketType, creationDate.toLocalDate(), price));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return busTicket;
    }

    @Override
    public Optional<BusTicket> fetchTicketByUserId(int user_id) {
        Optional<BusTicket> busTicket = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.SELECT_TICKET_BY_USER_ID);
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String ticketClass = resultSet.getString("ticket_class");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                Date creationDate = resultSet.getDate("creation_date");
                BigDecimal price = resultSet.getBigDecimal("price");

                busTicket = Optional.of(new BusTicket(id, user_id, ticketClass, ticketType, creationDate.toLocalDate(), price));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return busTicket;
    }

    @Override
    public void updateTicketTypeById(int id, TicketType ticketType) {
        String sql =
                "UPDATE tickets " +
                "SET ticket_type = ? " +
                "WHERE ticket_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.UPDATE_TICKET_TYPE_BY_ID);
            preparedStatement.setObject(1, ticketType.name(), Types.OTHER);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void saveTicketFromFile(BusTicket busTicket) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.INSERT_TICKET_FROM_FILE);
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
                "users (id, name, creation_date) " +
                "VALUES (?, ?, ?)";
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
