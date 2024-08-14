package org.abzal1.dao.ticket;

import org.abzal1.model.ticket.TicketType;

import java.util.Optional;

public interface TicketDao<T> {
    void saveTicket(T t, int user_id);
    Optional<T> fetchTicketById(int id);
    Optional<T> fetchTicketByUserId(int id);
    void updateTicketTypeById(int id, TicketType ticketType);
}
