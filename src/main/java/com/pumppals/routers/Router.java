package com.pumppals.routers;

import com.pumppals.annotations.RouteController;
import com.google.inject.Injector;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Handler;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class Router {

    public static void configureRoutes(Javalin app, Injector injector) {
        Reflections reflections = new Reflections("com.pumppals.controllers");
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(RouteController.class);

        for (Class<?> controllerClass : controllerClasses) {
            Object controllerInstance = injector.getInstance(controllerClass);

            for (Method method : controllerClass.getDeclaredMethods()) {
                String routePath = "/" + method.getName().toLowerCase();
                Handler handler = ctx -> method.invoke(controllerInstance, ctx);

                if (method.getName().startsWith("get")) {
                    app.get(routePath, handler);
                } else if (method.getName().startsWith("post")) {
                    app.post(routePath, handler);
                } else if (method.getName().startsWith("put")) {
                    app.put(routePath, handler);
                } else if (method.getName().startsWith("delete")) {
                    app.delete(routePath, handler);
                }
            }
        }
    }
}
