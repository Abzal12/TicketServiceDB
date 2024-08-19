package org.abzal1.dao.ticket;


import org.abzal1.model.ticket.BusTicket;
import org.abzal1.model.ticket.TicketType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TicketDaoImpl implements TicketDao<BusTicket> {

    private final JdbcTemplate jdbcTemplate;

    public TicketDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveTicket(BusTicket busTicket) {
        jdbcTemplate.update(
                TicketSQLQueries.INSERT_TICKET,
                busTicket.getId(),
                busTicket.getUser_id(),
                busTicket.getTicketType().name(),
                Date.valueOf(busTicket.getStartDate()),
                busTicket.getPrice());
    }

    @Override
    public Optional<BusTicket> fetchTicketById(int id) {
        return jdbcTemplate.queryForObject(
                TicketSQLQueries.SELECT_TICKET_BY_ID,
                (rs, rowNum) -> Optional.of(new BusTicket(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        TicketType.valueOf(rs.getString("ticket_type")),
                        rs.getDate("creation_date").toLocalDate(),
                        rs.getBigDecimal("price")
                )),
                id
        );
    }

    @Override
    public List<BusTicket> fetchTicketByUserId(int userId) {
        return jdbcTemplate.query(
                TicketSQLQueries.SELECT_TICKET_BY_USER_ID,
                (rs, rowNum) -> new BusTicket(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        TicketType.valueOf(rs.getString("ticket_type")),
                        rs.getDate("creation_date").toLocalDate(),
                        rs.getBigDecimal("price")
                ),
                userId
        );
    }

    @Override
    public void updateTicketTypeById(int id, TicketType ticketType) {
        jdbcTemplate.update(
                TicketSQLQueries.UPDATE_TICKET_TYPE_BY_ID,
                ticketType.name(),
                id
        );
    }
}
