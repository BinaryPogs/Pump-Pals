package com.pumppals;

import com.pumppals.config.AppConfig;
import com.pumppals.controllers.ChallengeController;
import com.pumppals.controllers.UserController;
import com.pumppals.database.DatabaseManager;
import com.pumppals.injection.InjectorProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pumppals.serialisation.JacksonConfig;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

import static com.pumppals.routers.Router.configureRoutes;

public class PumpPalsApp {
    public static void main(String[] args) {
        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            // Ensure shutdown is called when the application exits
            Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::shutdown));
        }

        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.jsonMapper(new JavalinJackson(JacksonConfig.createObjectMapper()));
        }).start(7000);

        // Handle CORS
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            ctx.header("Access-Control-Allow-Headers", "Content-Type");
        });

        app.get("/", ctx -> ctx.result("PumpPals API"));

        // Configure routes using the Router class
        configureRoutes(app, InjectorProvider.getInjector());
    }
}