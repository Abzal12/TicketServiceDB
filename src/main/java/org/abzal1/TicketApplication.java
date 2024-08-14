package org.abzal1;

import org.abzal1.dao.user.UserDaoImpl;
import org.abzal1.model.ticket.DataPath;

import org.abzal1.dao.ticket.TicketDaoImpl;
import org.abzal1.dao.connection.JdbcConnection;
import org.abzal1.model.ticket.BusTicket;
import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;
import org.abzal1.service.BusTicketService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

public class TicketApplication {

    public static void main(String[] args) {

        JdbcConnection jdbcConnection = new JdbcConnection();
        TicketDaoImpl ticketDaoImpl = new TicketDaoImpl(jdbcConnection);
        UserDaoImpl userDaoImpl = new UserDaoImpl(jdbcConnection);

        ticketDaoImpl.createTicketType();
        ticketDaoImpl.createTables();

//      with translation support
        Connection localConnection = null;
        try {
            localConnection = jdbcConnection.getConnection();
            localConnection.setAutoCommit(false);
            Savepoint savepoint1 = localConnection.setSavepoint("Savepoint1");
            try {
                User user = new User("user1");
                userDaoImpl.saveUser(user);
                System.out.println("user 1 id = " + user.getId());
                ticketDaoImpl.saveTicket(new BusTicket("ticketClass123", TicketType.YEAR, BigDecimal.valueOf(1000)), user.getId());
                localConnection.commit();
                System.out.println("User and ticket linked to each other successfully.");
            } catch (SQLException e) {
                localConnection.rollback(savepoint1);
                System.out.println("Transaction rolled back to savepoint1 due to: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        } catch (SQLException e) {
            try {
                localConnection.rollback();
            } catch (SQLException ex) {
                System.out.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            } catch (Exception exc) {
                System.err.println("Unexpected error: " + exc.getMessage());
            }
            System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } finally {
            try {
                if (localConnection != null) {
                    localConnection.close();
                }
            } catch (SQLException e) {
                System.out.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }

//      without translation support
        User user2 = new User("user2");
        userDaoImpl.saveUser(user2);
        System.out.println("user 2 id = " + user2.getId());
        ticketDaoImpl.saveTicket(new BusTicket("ticketClass2", TicketType.DAY, BigDecimal.valueOf(300.50)), user2.getId());

        System.out.println("Ticket with ticket_id 1: " + ticketDaoImpl.fetchTicketById(1));
        System.out.println("Ticket with user_id 1: " + ticketDaoImpl.fetchTicketByUserId(1));
        System.out.println("User with user_id 1: " + userDaoImpl.fetchUserById(1));
        ticketDaoImpl.updateTicketTypeById(1, TicketType.WEEK);
        userDaoImpl.deleteUserbyId(2);

//      transferring Ticket object records from file to db
        String filePath = DataPath.PATH_TO_DATA.getPath();
        try {
            List<BusTicket> busTicketList = BusTicketService.getObjectsFromFile(filePath);
            System.out.println("Successfully read " + busTicketList.size() + " tickets from file.");
            ticketDaoImpl.createAdminUser(new User(0, "admin"));
            System.out.println("Transferring valid tickets to the database (to the admin account)");
            for (BusTicket busTicket : busTicketList) {
                ticketDaoImpl.saveTicketFromFile(busTicket);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        jdbcConnection.closeConnection();
    }
}
