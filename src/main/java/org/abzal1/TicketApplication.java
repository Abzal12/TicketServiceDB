package org.abzal1;

import org.abzal1.dao.ticket.TicketDAOImpl;
import org.abzal1.dao.user.UserDAOImpl;
import org.abzal1.model.ticket.Ticket;
import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;
import org.abzal1.service.TicketService;
import org.abzal1.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class TicketApplication {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);
        UserDAOImpl userDAOImpl = context.getBean(UserDAOImpl.class);
        TicketDAOImpl ticketDAOImpl = context.getBean(TicketDAOImpl.class);
        UserService userService = context.getBean(UserService.class);

        User user = new User();
        user.setName("Abzal");

        Ticket ticket1 = new Ticket();
        ticket1.setTicketType(TicketType.DAY);
        ticket1.setTicketClass("VIP");
        ticket1.setUser(user);

        Ticket ticket2 = new Ticket();
        ticket2.setTicketType(TicketType.WEEK);
        ticket2.setTicketClass("Standard");
        ticket2.setUser(user);

        Ticket ticket3 = new Ticket();
        ticket3.setTicketType(TicketType.MONTH);
        ticket3.setTicketClass("Guest");
        ticket3.setUser(user);

        user.getTickets().add(ticket1);
        user.getTickets().add(ticket2);
        user.getTickets().add(ticket3);

        userDAOImpl.saveUser(user);

        //-------------------------------------------------------------
        List<Ticket> abzalTickets = ticketDAOImpl.fetchTicketsByUserId(1L);
        abzalTickets.forEach(System.out::println);

        userService.updateUserAndTicket(1L, "Tom", TicketType.WEEK);
        userDAOImpl.deleteUserById(1L);

        //      transferring Ticket object records from file to list
        TicketService ticketService = context.getBean(TicketService.class);
        ArrayList<Ticket> tickets = ticketService.loadTicketsFromFile("classpath:ticketData.txt");
        tickets.forEach(System.out::println);
    }
}