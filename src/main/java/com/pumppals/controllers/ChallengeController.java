package com.pumppals.controllers;

import io.javalin.http.Context;
import com.pumppals.models.Challenge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChallengeController {
    private static Map<UUID, Challenge> challenges = new HashMap<>();

    public static void getAllChallenges(Context ctx) {
        ctx.json(challenges.values());
    }

    public static void addChallenge(Context ctx) {
        Challenge challenge = ctx.bodyAsClass(Challenge.class);
        challenges.put(challenge.getId(), challenge);
        ctx.status(201).json(challenge);
    }

    public static void getChallenge(Context ctx) {
        try {
            String idString = ctx.pathParam("id");
            UUID id = UUID.fromString(idString);

            Challenge challenge = challenges.get(id);
            if (challenge != null) {
                ctx.json(challenge);
            }
            else {
                ctx.status(404).result("Challenge not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }

    public static void updateChallenge(Context ctx) {
        try {
            String idString = ctx.pathParam("id");
            UUID id = UUID.fromString(idString);

            Challenge updatedChallenge = ctx.bodyAsClass(Challenge.class);
            updatedChallenge.setId(id);

            if (challenges.containsKey(id)) {
                challenges.put(id, updatedChallenge);
                ctx.json(updatedChallenge);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }
    public static void deleteChallenge(Context ctx) {
        try {
            String idString = ctx.pathParam("id");
            UUID id = UUID.fromString(idString);

            if(challenges.containsKey(id)) {
                challenges.remove(id);
                ctx.status(204).result("");
            } else {
                ctx.status(404).result("Challenge not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }
}
