package com.jefferson.db;

import java.util.Optional;
import java.util.UUID;

public class MessageService {

    private final JPARepository repository;

    public MessageService(JPARepository repository) {
        this.repository = repository;
    }

    public void savePlayerMessage(UUID playerUuid, String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be empty");
        }
        if (text.length() > 256) {
            throw new IllegalArgumentException("Message too long");
        }

        repository.saveMessage(playerUuid, text.trim());
    }

    public Optional<MessageEntity> getMessageById(Long id) {
        return repository.findById(id);
    }
}
