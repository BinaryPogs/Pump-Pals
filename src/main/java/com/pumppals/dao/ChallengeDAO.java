package com.pumppals.dao;

import com.pumppals.database.DatabaseManager;
import com.pumppals.models.Challenge;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class ChallengeDAO {
    public Challenge createChallenge(Challenge challenge) {
        try (Session session = DatabaseManager.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(challenge);
            tx.commit();
            return challenge;
        }
    }
    public Challenge getChallengeById(UUID id){
        try (Session session = DatabaseManager.getSessionFactory().openSession()) {
            return session.get(Challenge.class, id);
        }
    }
    public List<Challenge> getAllChallenges() {
        try (Session session = DatabaseManager.getSessionFactory().openSession()) {
            return session.createQuery("FROM Challenge", Challenge.class).list();
        }
    }
    public void updateChallenge(Challenge challenge) {
        try (Session session = DatabaseManager.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(challenge);
            tx.commit();
        }
    }

    public void deleteChallenge(UUID id) {
        try (Session session = DatabaseManager.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Challenge challenge = session.get(Challenge.class, id);
            if (challenge != null){
                session.remove(id);
            }
            tx.commit();
        }
    }
}
