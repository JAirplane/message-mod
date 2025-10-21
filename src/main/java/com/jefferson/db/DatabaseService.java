package com.jefferson.db;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.UUID;

public class DatabaseService {
    private static final DatabaseService INSTANCE = new DatabaseService();

    public static DatabaseService getInstance() { return INSTANCE; }

    public void saveMessage(UUID uuid, String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            MessageEntity e = new MessageEntity(uuid, text);
            session.persist(e);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }
}
