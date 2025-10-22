package com.jefferson.db;

import com.jefferson.MessageMod;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class MessageRepository implements JPARepository {

    public static final MessageRepository messageRepository = new MessageRepository();

    public static MessageRepository getInstance() {
        return messageRepository;
    }

    @Override
    public void saveMessage(UUID uuid, String text) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            MessageEntity e = new MessageEntity(uuid, text);
            session.persist(e);
            tx.commit();
            MessageMod.LOGGER.info("Message saved for UUID: {}", uuid);
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            MessageMod.LOGGER.error("Failed to save message for UUID: {}", uuid, ex);
        }
    }

    @Override
    public Optional<MessageEntity> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            MessageEntity message = session.get(MessageEntity.class, id);
            return Optional.ofNullable(message);
        } catch (Exception ex) {
            MessageMod.LOGGER.error("Failed to find message by ID: {}", id, ex);
            return Optional.empty();
        }
    }
}
