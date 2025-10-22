package com.jefferson.db;

import java.util.Optional;
import java.util.UUID;

public interface JPARepository {
    void saveMessage(UUID uuid, String text);
    Optional<MessageEntity> findById(Long id);
}
