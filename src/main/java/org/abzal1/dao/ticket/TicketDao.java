package org.abzal1.dao.ticket;

import org.abzal1.model.ticket.TicketType;

import java.util.List;
import java.util.Optional;

public interface TicketDao<T> {
    void saveTicket(T t);
    Optional<T> fetchTicketById(int id);
    List<T> fetchTicketByUserId(int id);
    void updateTicketTypeById(int id, TicketType ticketType);
}
