package org.abzal1.dao.ticket;

import org.abzal1.model.ticket.Ticket;
import org.abzal1.model.ticket.TicketType;

import java.util.List;

public interface TicketDAO {

    void saveTicket(Ticket ticket);
    Ticket fetchTicketById(Long ticketId);
    List<Ticket> fetchTicketsByUserId(Long userId);
    void updateTicketTypeByUserId(long id, TicketType ticketType);
}
