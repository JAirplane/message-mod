package com.jefferson.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            Properties settings = loadDatabaseProperties();

            configuration.setProperties(settings);
            configuration.addAnnotatedClass(com.jefferson.db.MessageEntity.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties loadDatabaseProperties() {
        Properties properties = new Properties();

        try (InputStream input = HibernateUtil.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }

            properties.load(input);

            System.out.println("Loaded database properties:");
            System.out.println("URL: " + properties.getProperty("db.url"));
            System.out.println("Username: " + properties.getProperty("db.username"));

        } catch (IOException ex) {
            throw new RuntimeException("Error loading database properties", ex);
        }

        return properties;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory closed");
        }
    }
}