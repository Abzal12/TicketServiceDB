package org.abzal1;

import org.abzal1.dao.TicketDAO;
import org.abzal1.dao.UserDAO;
import org.abzal1.entity.ticket.Ticket;
import org.abzal1.entity.ticket.TicketType;
import org.abzal1.entity.user.User;

import java.util.List;

public class TicketApplication {
    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();
        TicketDAO ticketDAO = new TicketDAO();

        User user = new User();
        user.setName("Abzal");

        Ticket ticket1 = new Ticket();
        ticket1.setTicketType(TicketType.DAY);
        ticket1.setUser(user);

        Ticket ticket2 = new Ticket();
        ticket2.setTicketType(TicketType.WEEK);
        ticket2.setUser(user);

        Ticket ticket3 = new Ticket();
        ticket3.setTicketType(TicketType.MONTH);
        ticket3.setUser(user);

        user.getTickets().add(ticket1);
        user.getTickets().add(ticket2);
        user.getTickets().add(ticket3);

        userDAO.saveUser(user);

        //-------------------------------------------------------------

        user = userDAO.fetchUserById(1L);
        user.setName("Sam");
        user.getTickets().forEach(ticket -> ticket.setTicketType(TicketType.YEAR));
        userDAO.updateUserAndTickets(user);

        //-------------------------------------------------------------
        List<Ticket> abzalTickets = ticketDAO.fetchTicketsByUserId(1L);
        abzalTickets.forEach(System.out::println);
        userDAO.deleteUserById(1L);

    }
}