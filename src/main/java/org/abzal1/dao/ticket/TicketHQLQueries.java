package org.abzal1.dao.ticket;

public class TicketHQLQueries {

    public static final String SELECT_TICKET_BY_USER_ID =
            "SELECT * " +
                    "FROM tickets " +
                    "WHERE user_id = ?;";
}
