package org.abzal1.dao.ticket;

import org.abzal1.model.ticket.Ticket;
import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketDAOImpl implements TicketDAO {

    private final SessionFactory sessionFactory;

    public TicketDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveTicket(Ticket ticket) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
        }
    }

    @Override
    public Ticket fetchTicketById(Long ticketId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Ticket.class, ticketId);
        }
    }

    @Override
    public List<Ticket> fetchTicketsByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Ticket> query = session.createQuery(TicketHQLQueries.SELECT_TICKET_BY_USER_ID, Ticket.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public void updateTicketTypeByUserId(long id, TicketType ticketType) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.createQuery(TicketHQLQueries.UPDATE_TICKET_TYPE_BY_USER_ID);
            query.setParameter("ticketType", ticketType);
            query.setParameter("userId", id);
            query.executeUpdate();
            transaction.commit();
        }
    }
}

