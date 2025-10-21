package com.jefferson.db;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "text", nullable = false, length = 256)
    private String text;

    public MessageEntity() {}

    public MessageEntity(UUID uuid, String text) {
        this.uuid = uuid;
        this.text = text;
    }

    public Long getId() { return id; }
    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
