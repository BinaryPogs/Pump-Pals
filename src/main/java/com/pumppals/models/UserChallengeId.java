package com.pumppals.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UserChallengeId implements Serializable {
    private UUID userId;
    private UUID challengeId;

    public UserChallengeId() {}

    public UserChallengeId(UUID userId, UUID challengeId) {
        this.userId = userId;
        this.challengeId = challengeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(UUID challengeId) {
        this.challengeId = challengeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChallengeId that = (UserChallengeId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(challengeId, that.challengeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, challengeId);
    }

    @Override
    public String toString() {
        return "UserChallengeId{" +
                "userId=" + userId +
                ", challengeId=" + challengeId +
                '}';
    }
}