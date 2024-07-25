package com.pumppals.dao;

import com.pumppals.dao.interfaces.IChallengeDao;
import com.pumppals.database.DatabaseManager;
import com.pumppals.database.TransactionManager;
import com.pumppals.models.Challenge;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChallengeDao implements IChallengeDao {
    private static final Logger logger = LoggerFactory.getLogger(ChallengeDao.class);

    @Override
    public Challenge createChallenge(Challenge challenge) {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                session.persist(challenge);
                return challenge;
            });
        } catch (Exception e) {
            logger.error("Error creating challenge: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to create challenge", e);
        }
    }

    @Override
    public Optional<Challenge> getChallengeById(UUID id) {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                Challenge challenge = session.get(Challenge.class, id);
                return Optional.ofNullable(challenge);
            });
        } catch (Exception e) {
            logger.error("Error retrieving challenge with ID {}: {}", id, e.getMessage(), e);
            throw new IllegalStateException("Failed to retrieve challenge", e);
        }
    }

    @Override
    public List<Challenge> getAllChallenges() {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                return session.createQuery("FROM Challenge", Challenge.class).list();
            });
        } catch (Exception e) {
            logger.error("Error retrieving all challenges: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to retrieve all challenges", e);
        }
    }

    @Override
    public void updateChallenge(Challenge challenge) {
        try {
            TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                session.merge(challenge);
            });
        } catch (Exception e) {
            logger.error("Error updating challenge with ID {}: {}", challenge.getId(), e.getMessage(), e);
            throw new IllegalStateException("Failed to update challenge", e);
        }
    }

    @Override
    public boolean deleteChallenge(UUID id) {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                Challenge challenge = session.get(Challenge.class, id);
                if (challenge != null) {
                    session.remove(challenge);
                    return true;
                } else {
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting challenge with ID {}: {}", id, e.getMessage(), e);
            throw new IllegalStateException("Failed to delete challenge", e);
        }
    }
}