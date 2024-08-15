package org.abzal1.dao;

import org.abzal1.entity.ticket.Ticket;
import org.abzal1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TicketDAO {

    public void saveTicket(Ticket ticket) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
        }
    }

    public Ticket fetchTicketById(Long ticketId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Ticket.class, ticketId);
        }
    }

    public List<Ticket> fetchTicketsByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Ticket WHERE user.id = :userId";
            Query<Ticket> query = session.createQuery(hql, Ticket.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }
}

