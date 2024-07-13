package com.pumppals.dao;

import com.pumppals.config.DatabaseConfig;
import com.pumppals.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
public class UserDAO {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    public User createUser(User user) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user;
        }
    }
    public User getUserById(UUID id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM USER", User.class).list();
        }
    }
    public void updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        }
    }
    public void deleteUser(UUID id){
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        }
    }
}
