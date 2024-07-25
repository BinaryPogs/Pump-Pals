package com.pumppals;

import com.google.inject.Injector;
import com.pumppals.config.AppConfig;
import com.pumppals.routers.Router;
import io.javalin.Javalin;
import com.google.inject.Guice;

public class AppInitialiser {
    private Javalin app;

    public AppInitialiser() {
        Injector injector = Guice.createInjector(new AppConfig());

        // Initialize Javalin
        app = Javalin.create().start(7000);

        // Configure routes dynamically
        Router.configureRoutes(app, injector);
    }

    public Javalin getApp() {
        return app;
    }
}
