package com.pumppals.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pumppals.config.AppConfig;
import com.pumppals.controllers.ChallengeController;
import com.pumppals.controllers.UserController;

public class InjectorProvider {
    private static final Injector injector = Guice.createInjector(new AppConfig());

    public static Injector getInjector() {
        return injector;
    }

    public static UserController getUserController() {
        return injector.getInstance(UserController.class);
    }

    public static ChallengeController getChallengeController() {
        return injector.getInstance(ChallengeController.class);
    }
}