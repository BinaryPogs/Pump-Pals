package com.pumppals.models;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "userChallenges")
public class UserChallenge {
    @EmbeddedId
    private UserChallengeId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("challengeId")
    @JoinColumn(name = "challengeId")
    private Challenge challenge;

    @Column(nullable = false)
    private LocalDateTime joinDate;

    @ElementCollection
    @CollectionTable(name = "dailyProgress", joinColumns = {@JoinColumn(name = "userId"), @JoinColumn(name = "challengeId")})
    @MapKeyColumn(name = "date")
    @Column(name = "count")
    private Map<LocalDate, Integer> dailyProgress;

    public UserChallenge() {}

    public UserChallenge(User user, Challenge challenge, LocalDateTime joinDate) {
        this.id = new UserChallengeId(user.getId(), challenge.getId());
        this.user = user;
        this.challenge = challenge;
        this.joinDate = joinDate;
    }

    public UserChallengeId getId() {
        return id;
    }

    public void setId(UserChallengeId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Map<LocalDate, Integer> getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(Map<LocalDate, Integer> dailyProgress) {
        this.dailyProgress = dailyProgress;
    }

    @Override
    public String toString() {
        return "UserChallenge{" +
                "id=" + id +
                ", joinDate=" + joinDate +
                '}';
    }
}