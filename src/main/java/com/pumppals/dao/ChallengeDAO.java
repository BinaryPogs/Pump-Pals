package com.pumppals.dao;

import com.pumppals.models.Challenge;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.UUID;

public class ChallengeDAO {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public Challenge createChallenge(Challenge challenge) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(challenge);
            tx.commit();
            return challenge;
        }
    }
    public Challenge getChallengeById(UUID id){
        try (Session session = sessionFactory.openSession()) {
            return session.get(Challenge.class, id);
        }
    }
    public List<Challenge> getAllChallenges() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Challenge", Challenge.class).list();
        }
    }
    public void updateChallenge(Challenge challenge) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(challenge);
            tx.commit();
        }
    }

    public void deleteChallenge(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Challenge challenge = session.get(Challenge.class, id);
            if (challenge != null){
                session.remove(id);
            }
            tx.commit();
        }
    }
}
