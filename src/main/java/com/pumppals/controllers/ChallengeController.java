package com.pumppals.controllers;

import com.pumppals.annotations.RouteController;
import com.pumppals.annotations.Route;
import com.pumppals.dao.ChallengeDao;
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
        ctx.json(challengeDao.getAllChallenges());
    }

    @Route(path = "/challenges", method = "POST")
    public void addChallenge(Context ctx) {
        Challenge challenge = ctx.bodyAsClass(Challenge.class);
        Challenge createdChallenge = challengeDao.createChallenge(challenge);
        ctx.status(201).json(createdChallenge);
    }

    @Route(path = "/challenges/{id}", method = "GET")
    public void getChallenge(Context ctx) {
        String idString = ctx.pathParam("id");
        UUID id = UUID.fromString(idString);
        challengeDao.getChallengeById(id).ifPresentOrElse(
                ctx::json,
                () -> ctx.status(404).result("Challenge not found")
        );
    }

    @Route(path = "/challenges/{id}", method = "PUT")
    public void updateChallenge(Context ctx) {
        String idString = ctx.pathParam("id");
        UUID id = UUID.fromString(idString);
        Challenge updatedChallenge = ctx.bodyAsClass(Challenge.class);
        updatedChallenge.setId(id);
        challengeDao.updateChallenge(updatedChallenge);
        ctx.json(updatedChallenge);
    }

    @Route(path = "/challenges/{id}", method = "DELETE")
    public void deleteChallenge(Context ctx) {
        String idString = ctx.pathParam("id");
        UUID id = UUID.fromString(idString);
        if (challengeDao.deleteChallenge(id)) {
            ctx.status(204);
        } else {
            ctx.status(404).result("Challenge not found");
        }
    }
}