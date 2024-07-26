package com.pumppals.dao.interfaces;
import com.pumppals.models.Challenge;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IChallengeDao {
    Challenge createChallenge(Challenge challenge);
    Optional<Challenge> getChallengeById(UUID id);  // Using Optional<Challenge>
    List<Challenge> getAllChallenges();
    void updateChallenge(Challenge challenge);
    boolean deleteChallenge(UUID id);
}
