package com.pumppals.controllers;

import com.pumppals.annotations.RouteController;
import com.pumppals.annotations.Route;
import com.pumppals.dao.UserDao;
import io.javalin.http.Context;
import com.pumppals.models.User;

import javax.inject.Inject;
import java.util.UUID;

@RouteController
public class UserController {
    private final UserDao userDao;

    @Inject
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Route(path = "/users", method = "GET")
    public void getAllUsers(Context ctx) {
        ctx.json(userDao.getAllUsers());
    }

    @Route(path = "/users", method = "POST")
    public void addUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        user.setId(UUID.randomUUID());
        try {
            var createdUser = userDao.createUser(user);
            ctx.status(201).json(createdUser);
        } catch (RuntimeException e) {  // Use RuntimeException if your DAO now throws unchecked exceptions
            ctx.status(500).result("Error creating user: " + e.getMessage());
        }
    }

    @Route(path = "/users/{id}", method = "GET")
    public void getUser(Context ctx) {
        String idString = ctx.pathParam("id");
        try {
            UUID id = UUID.fromString(idString);
            userDao.getUserById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("User not found")
            );
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }

    @Route(path = "/users/{id}", method = "PUT")
    public void updateUser(Context ctx) {
        String idString = ctx.pathParam("id");
        try {
            UUID id = UUID.fromString(idString);
            User updatedUser = ctx.bodyAsClass(User.class);
            updatedUser.setId(id);

            if (userDao.getUserById(id).isPresent()) {
                userDao.updateUser(updatedUser);
                ctx.json(updatedUser);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }

    @Route(path = "/users/{id}", method = "DELETE")
    public void deleteUser(Context ctx) {
        String idString = ctx.pathParam("id");
        try {
            UUID id = UUID.fromString(idString);

            if (userDao.getUserById(id).isPresent()) {
                userDao.deleteUser(id);
                ctx.status(204);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid UUID format");
        }
    }
}
