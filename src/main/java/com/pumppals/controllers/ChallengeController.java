package com.pumppals.controllers;

import io.javalin.http.Context;
import com.pumppals.models.Challenge;

import java.util.HashMap;
import java.util.Map;

public class ChallengeController {
    private static Map<String, Challenge> challenges = new HashMap<>();

    public static void getAllChallenges(Context ctx) {
        ctx.json(challenges.values());
    }

    public static void addChallenge(Context ctx) {
        Challenge challenge = ctx.bodyAsClass(Challenge.class);
        challenges.put(challenge.getId(), challenge);
        ctx.status(201).json(challenge);
    }

    public static void getChallenge(Context ctx) {
        String id = ctx.pathParam("id");
        Challenge challenge = challenges.get(id);
        if (challenge != null) {
            ctx.json(challenge);
        } else {
            ctx.status(404).result("Challenge not found");
        }
    }

    public static void updateChallenge(Context ctx) {
        String id = ctx.pathParam("id");
        Challenge updatedChallenge = ctx.bodyAsClass(Challenge.class);
        challenges.put(id, updatedChallenge);
        ctx.json(updatedChallenge);
    }

    public static void deleteChallenge(Context ctx) {
        String id = ctx.pathParam("id");
        challenges.remove(id);
        ctx.status(204).result("");
    }
}
