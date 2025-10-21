package com.jefferson;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.*;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK;

public class MessageModClient implements ClientModInitializer {

    private static KeyMapping openKey;

	@Override
	public void onInitializeClient() {

        openKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.message_mod.toggle_message",
                GLFW.GLFW_KEY_M,
                "category.message_mod"
        ));

        END_CLIENT_TICK.register(client -> {
            while (openKey.consumeClick()) {
                client.setScreen(new MessageScreen());
            }
        });
	}
}