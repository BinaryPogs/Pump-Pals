package com.pumppals.dao;

import com.pumppals.dao.exceptions.ChallengeDatabaseException;
import com.pumppals.dao.interfaces.IChallengeDao;
import com.pumppals.database.TransactionManager;
import com.pumppals.models.Challenge;
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
            return TransactionManager.executeInTransaction(session -> {
                session.persist(challenge);
                session.flush();
                return challenge;
            });
        } catch (Exception e) {
            logger.error("Error creating challenge: {}", e.getMessage(), e);
            throw new ChallengeDatabaseException("Failed to create challenge", e);
        }
    }

    @Override
    public Optional<Challenge> getChallengeById(UUID id) {
        try {
            return TransactionManager.executeInTransaction(session ->
                    Optional.ofNullable(session.get(Challenge.class, id))
            );
        } catch (Exception e) {
            logger.error("Error retrieving challenge with ID {}: {}", id, e.getMessage(), e);
            throw new ChallengeDatabaseException("Failed to retrieve challenge", e);
        }
    }

    @Override
    public List<Challenge> getAllChallenges() {
        try {
            return TransactionManager.executeInTransaction(session ->
                    session.createQuery("FROM Challenge", Challenge.class).list()
            );
        } catch (Exception e) {
            logger.error("Error retrieving all challenges: {}", e.getMessage(), e);
            throw new ChallengeDatabaseException("Failed to retrieve all challenges", e);
        }
    }

    @Override
    public void updateChallenge(Challenge challenge) {
        try {
            TransactionManager.executeInTransaction(session -> {
                if (session.get(Challenge.class, challenge.getId()) == null) {
                    throw new ChallengeDatabaseException("Challenge not found with ID: " + challenge.getId());
                }
                session.merge(challenge);
                return null;
            });
        } catch (ChallengeDatabaseException e) {
            throw e; // Re-throw the exception if it's already a ChallengeDatabaseException
        } catch (Exception e) {
            logger.error("Error updating challenge with ID {}: {}", challenge.getId(), e.getMessage(), e);
            throw new ChallengeDatabaseException("Failed to update challenge", e);
        }
    }

    @Override
    public boolean deleteChallenge(UUID id) {
        try {
            return TransactionManager.executeInTransaction(session -> {
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
            throw new ChallengeDatabaseException("Failed to delete challenge", e);
        }
    }
}