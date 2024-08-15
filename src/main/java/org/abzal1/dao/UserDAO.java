package org.abzal1.dao;

import org.abzal1.entity.ticket.Ticket;
import org.abzal1.entity.user.User;
import org.abzal1.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDAO {

    public void saveUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    public User fetchUserById(Long userId) {
        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, userId);
            // Initialize the tickets collection
            Hibernate.initialize(user.getTickets());
        }
        return user;
    }

    public void deleteUserById(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        }
    }

    public void updateUserAndTickets(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.update(user);
            for (Ticket ticket : user.getTickets()) {
                session.update(ticket);
            }

            transaction.commit();
        }
    }
}
