package com.pumppals.controllers;

import com.pumppals.annotations.RouteController;
import com.pumppals.dao.UserDao;
import io.javalin.http.Context;
import com.pumppals.models.User;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.UUID;

@RouteController
public class UserController {
    private final UserDao userDao;

    @Inject
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    public void getAllUsers(Context ctx) {
        ctx.json(userDao.getAllUsers());
    }

    public void addUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        user.setId(UUID.randomUUID()); // Set a new UUID for the user
        try {
            var createdUser = userDao.createUser(user);
            ctx.status(201).json(createdUser);
        } catch (SQLException e) {
            ctx.status(500).result("Error creating user");
        }
    }

    public void getUser(Context ctx) {
        String idString = ctx.pathParam("id");
        try {
            UUID id = UUID.fromString(idString);
            userDao.getUserById(id).ifPresentOrElse(
                    ctx::json,
                    () -> ctx.status(404).result("User not found")
            );
        } catch (IllegalArgumentException | SQLException e) {
            ctx.status(400).result("Invalid UUID format or error fetching user");
        }
    }

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
        } catch (IllegalArgumentException | SQLException e) {
            ctx.status(400).result("Invalid UUID format or error updating user");
        }
    }

    public void deleteUser(Context ctx) {
        String idString = ctx.pathParam("id");
        try {
            UUID id = UUID.fromString(idString);

            if (userDao.getUserById(id).isPresent()) {
                userDao.deleteUser(id);
                ctx.status(204).result("");
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (IllegalArgumentException | SQLException e) {
            ctx.status(400).result("Invalid UUID format or error deleting user");
        }
    }
}
