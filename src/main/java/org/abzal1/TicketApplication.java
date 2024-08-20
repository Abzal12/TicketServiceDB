package org.abzal1;

import org.abzal1.dao.ticket.TicketDao;
import org.abzal1.dao.user.UserDao;
import org.abzal1.model.ticket.BusTicket;
import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.math.BigDecimal;

public class TicketApplication {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);
            UserDao userDao = context.getBean(UserDao.class);
            TicketDao ticketDao = context.getBean(TicketDao.class);

            User user = new User();
            user.setName("Abzal");
            userDao.saveUser(user);

            BusTicket busTicket = new BusTicket();
            busTicket.setUser_id(user.getId());
            busTicket.setTicketType(TicketType.YEAR);
            busTicket.setPrice(BigDecimal.valueOf(1000L));
            ticketDao.saveTicket(busTicket);

            BusTicket busTicket2 = new BusTicket();
            busTicket2.setUser_id(user.getId());
            busTicket2.setTicketType(TicketType.MONTH);
            busTicket2.setPrice(BigDecimal.valueOf(100L));
            ticketDao.saveTicket(busTicket2);

            System.out.println("fetchTicketById(1): " + ticketDao.fetchTicketById(1));
            System.out.println("fetchTicketByUserId(1): " + ticketDao.fetchTicketByUserId(1));
            ticketDao.updateTicketTypeById(2, TicketType.WEEK);
            System.out.println("fetchTicketById(2): " + ticketDao.fetchTicketById(2));
            System.out.println(userDao.fetchUserById(1));
            userDao.deleteUserbyId(1);
    }
}
