package com.jefferson.db;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface JPARepository {
    CompletableFuture<Void> saveMessage(UUID uuid, String text);
    CompletableFuture<Optional<MessageEntity>> findById(Long id);
}
