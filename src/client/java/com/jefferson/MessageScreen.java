package com.jefferson;

import com.jefferson.networking.MessagePayload;
import com.jefferson.protobuf.MessageOuterClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class MessageScreen extends Screen {

    private EditBox textField;
    private Button sendButton;
    public static final ResourceLocation MESSAGE_CHANNEL =
            ResourceLocation.fromNamespaceAndPath("message_mod", "message");

    public MessageScreen() {
        super(Component.literal("Send Message"));
    }

    @Override
    protected void init() {

        super.init();

        int widthCenter = this.width / 2;
        int heightCenter = this.height / 2;

        textField = new EditBox(this.font, widthCenter - 100, heightCenter - 10, 200, 20,
                Component.literal("Enter message"));
        this.addRenderableWidget(textField);

        sendButton = Button.builder(Component.literal("Send"), button -> {
            String messageText = textField.getValue();
            sendMessageToServer(messageText);
            this.onClose();
        }).bounds(widthCenter - 50, heightCenter + 20, 100, 20).build();
        this.addRenderableWidget(sendButton);

        setInitialFocus(textField);
    }

    private void sendMessageToServer(String messageText) {
        try {
            MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                    .setText(messageText)
                    .build();

            byte[] bytes = message.toByteArray();

            MessagePayload payload = new MessagePayload(bytes);
            ClientPlayNetworking.send(payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.drawString(this.font, "Message:", this.width / 2 - 100, this.height / 2 - 25, 0xFFFFFF);
    }

}
