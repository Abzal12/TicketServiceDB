package org.abzal1.dao;

import org.abzal1.model.ticket.TicketType;

import java.util.Optional;

public interface Dao<T, I> {
    void saveTicket(T t);
    void saveUser(I i);
    Optional<T> fetchTicketById(int id);
    Optional<T> fetchTicketByUserId(int id);
    Optional<I> fetchUserById(int id);
    void updateTicketTypeById(int id, TicketType ticketType);
    void deleteUserbyId(int id);
}
