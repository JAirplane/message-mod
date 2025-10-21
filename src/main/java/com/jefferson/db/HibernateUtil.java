package com.jefferson.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = build();

    private static SessionFactory build() {
        try {
            Configuration cfg = new Configuration();

            Properties props = new Properties();
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/minecraftdb");
            props.put("hibernate.connection.username", "minecraft_user");
            props.put("hibernate.connection.password", "secret");
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.hbm2ddl.auto", "validate");
            props.put("hibernate.show_sql", "true");

            cfg.setProperties(props);
            cfg.addAnnotatedClass(MessageEntity.class);

            return cfg.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() { return sessionFactory; }
}
