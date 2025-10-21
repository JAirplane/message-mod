package com.jefferson;

import com.jefferson.protobuf.MessageOuterClass;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MessageMod implements ModInitializer {
	public static final String MOD_ID = "message-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        ServerPlayNetworking.registerGlobalReceiver(
                MessageScreen.MessagePayload.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    byte[] data = payload.data();

                    Objects.requireNonNull(player.getServer()).execute(() -> {
                        try {
                            MessageOuterClass.Message message = MessageOuterClass.Message.parseFrom(data);
                            String text = message.getText();

                            // Обработка сообщения
                            player.sendSystemMessage(Component.literal("You said: " + text));

                            for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
                                if (serverPlayer != player) {
                                    serverPlayer.sendSystemMessage(
                                            Component.literal("[Chat] " + player.getScoreboardName() + ": " + text)
                                    );
                                }
                            }

                        } catch (Exception e) {
                            LOGGER.error("Failed to parse message", e);
                        }
                    });
                }
        );
	}
}