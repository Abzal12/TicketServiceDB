package org.abzal1.dao.ticket;

public class TicketSQLQueries {
    public static final String SELECT_TICKET_BY_ID =
            "SELECT * " +
            "FROM tickets " +
            "WHERE id = ?;";
    public static final String INSERT_TICKET_FROM_FILE =
            "INSERT INTO " +
            "tickets (user_id, ticket_class, ticket_type, creation_date, price) " +
            "VALUES (?, ?, ?, ?, ?);";
    public static final String INSERT_TICKET =
            "INSERT INTO " +
            "tickets (user_id, ticket_class, ticket_type, creation_date, price) " +
            "VALUES (?, ?, ?, ?, ?) RETURNING id;";
    public static final String UPDATE_TICKET_TYPE_BY_ID =
            "UPDATE tickets " +
            "SET ticket_type = ? " +
            "WHERE id = ?;";
    public static final String SELECT_TICKET_BY_USER_ID =
            "SELECT * " +
            "FROM tickets " +
            "WHERE user_id = ?;";
}