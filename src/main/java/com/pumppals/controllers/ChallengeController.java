package com.pumppals.controllers;

import com.pumppals.annotations.RouteController;
import com.pumppals.annotations.Route;
import com.pumppals.controllers.response.ErrorResponse;
import com.pumppals.dao.ChallengeDao;
import com.pumppals.dao.exceptions.ChallengeDatabaseException;
import com.pumppals.dao.exceptions.ChallengeNotFoundException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import com.pumppals.models.Challenge;

import javax.inject.Inject;
import java.util.UUID;

@RouteController
public class ChallengeController {
    private final ChallengeDao challengeDao;

    @Inject
    public ChallengeController(ChallengeDao challengeDao) {
        this.challengeDao = challengeDao;
    }

    @Route(path = "/challenges", method = "GET")
    public void getAllChallenges(Context ctx) {
        try {
            ctx.json(challengeDao.getAllChallenges());
        } catch (ChallengeDatabaseException e) {
            ctx.status(500).json(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @Route(path = "/challenges", method = "POST")
    public void addChallenge(Context ctx) {
        try {
            Challenge challenge = ctx.bodyAsClass(Challenge.class);
            Challenge createdChallenge = challengeDao.createChallenge(challenge);
            ctx.status(201).json(createdChallenge);
        } catch (BadRequestResponse e) {
            // This will catch JSON parsing errors
            ctx.status(400).json(new ErrorResponse("Invalid challenge data: " + e.getMessage()));
        } catch (ChallengeDatabaseException e) {
            ctx.status(500).json(new ErrorResponse("Failed to create challenge: " + e.getMessage()));
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            ctx.status(500).json(new ErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @Route(path = "/challenges/{id}", method = "GET")
    public void getChallenge(Context ctx) {
        try {
            UUID id = UUID.fromString(ctx.pathParam("id"));
            challengeDao.getChallengeById(id)
                    .ifPresentOrElse(
                            ctx::json,
                            () -> ctx.status(404).json(new ErrorResponse("Challenge not found"))
                    );
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse("Invalid challenge ID format"));
        } catch (ChallengeDatabaseException e) {
            ctx.status(500).json(new ErrorResponse("An unexpected error occurred"));
        }
    }

    @Route(path = "/challenges/{id}", method = "PUT")
    public void updateChallenge(Context ctx) {
        try {
            UUID id = UUID.fromString(ctx.pathParam("id"));
            Challenge updatedChallenge = ctx.bodyAsClass(Challenge.class);
            updatedChallenge.setId(id);
            challengeDao.updateChallenge(updatedChallenge);
            ctx.json(updatedChallenge);
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse("Invalid challenge ID format"));
        } catch (ChallengeNotFoundException e) {
            ctx.status(404).json(new ErrorResponse(e.getMessage()));
        } catch (ChallengeDatabaseException e) {
            ctx.status(500).json(new ErrorResponse("Failed to update challenge"));
        }
    }

    @Route(path = "/challenges/{id}", method = "DELETE")
    public void deleteChallenge(Context ctx) {
        try {
            UUID id = UUID.fromString(ctx.pathParam("id"));
            boolean deleted = challengeDao.deleteChallenge(id);
            if (deleted) {
                ctx.status(204); // No Content
            } else {
                ctx.status(404).json(new ErrorResponse("Challenge not found"));
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(new ErrorResponse("Invalid challenge ID format"));
        } catch (ChallengeDatabaseException e) {
            ctx.status(500).json(new ErrorResponse("Failed to delete challenge"));
        }
    }
}