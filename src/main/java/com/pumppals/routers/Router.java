package com.pumppals.routers;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import com.pumppals.controllers.UserController;
import com.pumppals.controllers.ChallengeController;

public class Router {

    public static void configureRoutes(Javalin app) {
        app.routes(() -> {
            path("users", () -> {
                get(UserController::getAllUsers);
                post(UserController::addUser);
                path("{id}", () -> {
                    get(UserController::getUser);
                    put(UserController::updateUser);
                    delete(UserController::deleteUser);
                });
            });
            path("challenges", () -> {
                get(ChallengeController::getAllChallenges);
                post(ChallengeController::addChallenge);
                path("{id}", () -> {
                    get(ChallengeController::getChallenge);
                    put(ChallengeController::updateChallenge);
                    delete(ChallengeController::deleteChallenge);
                });
            });
        });
    }
}
