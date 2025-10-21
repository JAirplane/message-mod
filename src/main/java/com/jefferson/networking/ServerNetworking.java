package com.jefferson.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class ServerNetworking {

    public static final ResourceLocation MESSAGE_CHANNEL =
            ResourceLocation.fromNamespaceAndPath("template_mod", "message");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(MESSAGE_CHANNEL, (server, player, handler, buf, responseSender) -> {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);

            try {
                Message message = Message.parseFrom(bytes);

                server.execute(() -> saveMessage(player.getUuid(), message.getText()));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
    }

    private static void saveMessage(UUID playerUuid, String text) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            MessageEntity entity = new MessageEntity();
            entity.setUuid(playerUuid);
            entity.setText(text);
            session.persist(entity);
            tx.commit();
        }
    }
}
