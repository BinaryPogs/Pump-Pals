package com.pumppals.controllers;

import io.javalin.http.Context;
import com.pumppals.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserController {
    private static Map<UUID, User> users = new HashMap<>();
    public static void getAllUsers(Context ctx) {
        ctx.json(users.values());
    }
    public static void addUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        users.put(user.getId(), user);
        ctx.status(201).json(user);
    }

    public static void getUser(Context ctx) {
        try {
            String idString = ctx.pathParam("id");
            UUID id = UUID.fromString(idString);

            User user = users.get(id);
            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }
    public static void updateUser(Context ctx) {
        try {
            String idString = ctx.pathParam("id");
            UUID id = UUID.fromString(idString);

            User updatedUser = ctx.bodyAsClass(User.class);
            updatedUser.setId(id);

            if (users.containsKey(id)) {
                users.put(id, updatedUser);
                ctx.json(updatedUser);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }

    public static void deleteUser(Context ctx) {
        try {
            String idString = ctx.pathParam("id");
            UUID id = UUID.fromString(idString);

            if (users.containsKey(id)) {
                users.remove(id);
                ctx.status(204).result("");
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }
}
