package org.abzal1.dao.user;

import org.abzal1.model.ticket.TicketType;
import org.abzal1.model.user.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    @Override
    public User fetchUserById(Long userId) {
        User user;
        try (Session session = sessionFactory.openSession()) {
            user = session.get(User.class, userId);
            Hibernate.initialize(user.getTickets());
        }
        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        }
    }

//    public void updateUserAndTickets(User user) {
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            session.update(user);
//
//            transaction.commit();
//        }
//    }
//
//    public void updateUserNameAndTicketTypeByUserId(Long userId, String name, TicketType ticketType) {
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = session.beginTransaction();
//
//            User user = session.get(User.class, userId);
//            Hibernate.initialize(user.getTickets());
//            user.setName(name);
//            user.getTickets().getFirst().setTicketType(ticketType);
//            session.update(user);
//
//            transaction.commit();
//        }
//    }

    public void updateUserNameById(long id, String name) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            Hibernate.initialize(user.getTickets());
            user.setName(name);
            session.update(user);
            transaction.commit();
        }
    }
}
