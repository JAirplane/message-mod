package com.jefferson;

import com.jefferson.db.HibernateUtil;
import com.jefferson.db.MessageRepository;
import com.jefferson.db.MessageService;
import com.jefferson.networking.MessagePayload;
import com.jefferson.protobuf.MessageOuterClass;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class MessageMod implements ModInitializer {

    public static final String MOD_ID = "message-mod-id";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ResourceLocation MESSAGE_CHANNEL =
            ResourceLocation.fromNamespaceAndPath("message_mod", "message");

    private MessageService messageService;

    @Override
    public void onInitialize() {
        LOGGER.info("MessageMod initialized!");

        messageService = new MessageService(new MessageRepository());

        PayloadTypeRegistry.playC2S().register(MessagePayload.TYPE, MessagePayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(MessagePayload.TYPE, (payload, context) -> {
            handleMessage(payload, context.player());
        });
    }

    private void handleMessage(MessagePayload payload, ServerPlayer player) {
        try {
            MessageOuterClass.Message message = MessageOuterClass.Message.parseFrom(payload.data());
            String messageText = message.getText();
            UUID playerUUID = player.getUUID();

            messageService.savePlayerMessage(playerUUID, messageText);

            LOGGER.info("Received and saved message from {}: {}",
                    player.getGameProfile().getName(), messageText);

            player.sendSystemMessage(Component.literal("Message saved!"));

        } catch (Exception e) {
            LOGGER.error("Failed to process message from player {}", player.getGameProfile().getName(), e);
            player.sendSystemMessage(Component.literal("Failed to save message"));
        }
    }

    public static void onShutdown() {
        HibernateUtil.shutdown();
    }
}