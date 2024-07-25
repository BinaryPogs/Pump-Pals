package com.pumppals.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String exerciseType;

    @Column(nullable = false)
    private int dailyGoal;

    @Column(nullable = false)
    private int durationDays;

    @Column(nullable = false)
    private LocalDate startDate;

    @OneToMany(mappedBy = "challenge")
    private Set<UserChallenge> participants;

    public Challenge() {}

    public Challenge(String name, String exerciseType, int dailyGoal, int durationDays, LocalDate startDate) {
        this.name = name;
        this.exerciseType = exerciseType;
        this.dailyGoal = dailyGoal;
        this.durationDays = durationDays;
        this.startDate = startDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Set<UserChallenge> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserChallenge> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", exerciseType='" + exerciseType + '\'' +
                ", dailyGoal=" + dailyGoal +
                ", durationDays=" + durationDays +
                ", startDate=" + startDate +
                '}';
    }
}