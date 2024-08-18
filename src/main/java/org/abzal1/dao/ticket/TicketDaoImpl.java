package org.abzal1.dao.ticket;


import org.abzal1.model.ticket.BusTicket;
import org.abzal1.model.ticket.TicketType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TicketDaoImpl implements TicketDao<BusTicket> {

    private final Connection connection;

    public TicketDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveTicket(BusTicket busTicket) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.INSERT_TICKET);
            preparedStatement.setInt(1, busTicket.getId());
            preparedStatement.setInt(2, busTicket.getUser_id());
            preparedStatement.setObject(3, busTicket.getTicketType().name());
            preparedStatement.setDate(4, Date.valueOf(busTicket.getStartDate()));
            preparedStatement.setBigDecimal(5, busTicket.getPrice());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public Optional<BusTicket> fetchTicketById(int id) {
        Optional<BusTicket> busTicket = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.SELECT_TICKET_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                Date creationDate = resultSet.getDate("creation_date");
                BigDecimal price = resultSet.getBigDecimal("price");
                busTicket = Optional.of(new BusTicket(id, user_id, ticketType, creationDate.toLocalDate(), price));
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return busTicket;
    }

    @Override
    public List<BusTicket> fetchTicketByUserId(int userId) {
        List<BusTicket> busTickets = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.SELECT_TICKET_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int ticket_id = resultSet.getInt("id");
                    TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                    Date creationDate = resultSet.getDate("creation_date");
                    BigDecimal price = resultSet.getBigDecimal("price");
                    BusTicket busTicket = new BusTicket(ticket_id, userId, ticketType, creationDate.toLocalDate(), price);
                    busTickets.add(busTicket);
                }
            }
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
        return busTickets;
    }

    @Override
    public void updateTicketTypeById(int id, TicketType ticketType) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(TicketSQLQueries.UPDATE_TICKET_TYPE_BY_ID);
            preparedStatement.setObject(1, ticketType.name());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
