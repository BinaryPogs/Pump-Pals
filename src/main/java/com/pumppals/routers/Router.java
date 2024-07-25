package com.pumppals.routers;

import com.pumppals.annotations.RouteController;
import com.pumppals.annotations.Route;
import com.google.inject.Injector;
import io.javalin.Javalin;
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
                Route routeAnnotation = method.getAnnotation(Route.class);
                if (routeAnnotation != null) {
                    String path = routeAnnotation.path();
                    String httpMethod = routeAnnotation.method().toUpperCase();
                    Handler handler = ctx -> method.invoke(controllerInstance, ctx);

                    switch (httpMethod) {
                        case "GET":
                            app.get(path, handler);
                            break;
                        case "POST":
                            app.post(path, handler);
                            break;
                        case "PUT":
                            app.put(path, handler);
                            break;
                        case "DELETE":
                            app.delete(path, handler);
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
                    }
                }
            }
        }
    }
}