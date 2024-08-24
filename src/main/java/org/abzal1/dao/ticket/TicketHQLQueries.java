package org.abzal1.dao.ticket;

public class TicketHQLQueries {

    public static final String SELECT_TICKET_BY_USER_ID =
            "FROM Ticket " +
            "WHERE user.id = :userId";

    public static final String UPDATE_TICKET_TYPE_BY_USER_ID =
            "UPDATE Ticket SET ticketType = :ticketType WHERE user.id = :userId";
}
