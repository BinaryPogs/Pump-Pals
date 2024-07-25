package com.pumppals.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private DatabaseManager() {
        // Private constructor to prevent instantiation
    }
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return props;
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void shutdown() {
        getSessionFactory().close();
    }
    public static void initializeDatabase() {
        createDatabaseIfNotExists();
        updateSchema();
    }
    private static void createDatabaseIfNotExists() {
        if (DB_URL == null || DB_NAME == null || DB_USER == null || DB_PASSWORD == null) {
            logger.error("Database configuration is incomplete. Please set all required environment variables.");
            throw new IllegalStateException("Incomplete database configuration");
        }

        String baseUrl = DB_URL + DB_USER;
        logger.info("Attempting to connect to URL: {}", baseUrl);
        logger.info("Using username: {}", DB_USER);

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection connection = DriverManager.getConnection(baseUrl, DB_USER, DB_PASSWORD);
                 Statement statement = connection.createStatement()) {

                // Check if database exists
                ResultSet resultSet = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'");
                if (!resultSet.next()) {
                    // Database doesn't exist, so create it
                    statement.executeUpdate("CREATE DATABASE " + DB_NAME);
                    logger.info("Database '{}' created successfully", DB_NAME);
                } else {
                    logger.info("Database '{}' already exists", DB_NAME);
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC driver not found", e);
            throw new RuntimeException("JDBC driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to create database. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Database creation failed", e);
        }
    }

    private static void updateSchema() {
        try (Session session = getSessionFactory().openSession()) {
            logger.info("Database schema updated successfully");
        } catch (Exception e) {
            logger.error("Error updating database schema: {}", e.getMessage(), e);
            throw new RuntimeException("Schema update failed", e);
        }
    }

    public static Session openSession() {
        return getSessionFactory().openSession();
    }
}