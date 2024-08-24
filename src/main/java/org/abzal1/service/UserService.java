package org.abzal1.service;

import org.abzal1.dao.ticket.TicketDAOImpl;
import org.abzal1.dao.user.UserDAOImpl;
import org.abzal1.model.ticket.TicketType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Value("${user.ticket.update.enabled:true}")
    private boolean updateEnabled;

    private final UserDAOImpl userDAOImpl;
    private final TicketDAOImpl ticketDAOImpl;

    public UserService(UserDAOImpl userDAOImpl, TicketDAOImpl ticketDAOImpl) {
        this.userDAOImpl = userDAOImpl;
        this.ticketDAOImpl = ticketDAOImpl;
    }

    @Transactional
    public void updateUserAndTicket(Long userId, String name, TicketType ticketType) {

        if (!updateEnabled) {
            System.err.println("\nUpdating User and Ticket is disabled. Look at the config.properties file");
        } else {
            userDAOImpl.updateUserNameById(userId, name);
            ticketDAOImpl.updateTicketTypeByUserId(userId, ticketType);
        }
    }
}
