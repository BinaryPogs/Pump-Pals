package com.pumppals;

import io.javalin.Javalin;

import static com.pumppals.routers.Router.configureRoutes;

public class PumpPalsApp {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
        }).start(7000);

        // Handle CORS
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
        });

        app.get("/", ctx -> ctx.result("PumpPals API"));

        // Configure routes using the Router class
        configureRoutes(app);
    }
}
