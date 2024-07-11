package com.pumppals.controllers;

import io.javalin.http.Context;
import com.pumppals.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    private static Map<String, User> users = new HashMap<>();
    public static void getAllUsers(Context ctx) {
        ctx.json(users.values());
    }
    public static void addUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        users.put(user.getId(), user);
        ctx.status(201).json(user);
    }

    public static void getUser(Context ctx) {
        String id = ctx.pathParam("id");
        User user = users.get(id);
        if (user != null) {
            ctx.json(user);
        } else {
            ctx.status(404).result("User not found");
        }
    }

    public static void updateUser(Context ctx) {
        String id = ctx.pathParam("id");
        User updatedUser = ctx.bodyAsClass(User.class);
        users.put(id, updatedUser);
        ctx.json(updatedUser);
    }

    public static void deleteUser(Context ctx) {
        String id = ctx.pathParam("id");
        users.remove(id);
        ctx.status(204).result("");
    }
}
