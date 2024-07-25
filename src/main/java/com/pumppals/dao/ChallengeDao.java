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
                logger.info("Created new challenge with ID: {}", challenge.getId());
                return challenge;
            });
        } catch (Exception e) {
            logger.error("Error creating challenge: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create challenge", e);
        }
    }

    @Override
    public Optional<Challenge> getChallengeById(UUID id) {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                Challenge challenge = session.get(Challenge.class, id);
                if (challenge == null) {
                    logger.warn("No challenge found with ID: {}", id);
                }
                return Optional.ofNullable(challenge);
            });
        } catch (Exception e) {
            logger.error("Error retrieving challenge with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve challenge", e);
        }
    }

    @Override
    public List<Challenge> getAllChallenges() {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                List<Challenge> challenges = session.createQuery("FROM Challenge", Challenge.class).list();
                logger.info("Retrieved {} challenges", challenges.size());
                return challenges;
            });
        } catch (Exception e) {
            logger.error("Error retrieving all challenges: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve all challenges", e);
        }
    }

    @Override
    public void updateChallenge(Challenge challenge) {
        try {
            TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                session.merge(challenge);
                logger.info("Updated challenge with ID: {}", challenge.getId());
            });
        } catch (Exception e) {
            logger.error("Error updating challenge with ID {}: {}", challenge.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update challenge", e);
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
                    logger.info("Deleted challenge with ID: {}", id);
                    return true;
                } else {
                    logger.warn("Attempted to delete non-existent challenge with ID: {}", id);
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting challenge with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete challenge", e);
        }
    }
}