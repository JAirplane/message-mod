package com.jefferson;

import com.jefferson.protobuf.MessageOuterClass;
import com.mojang.blaze3d.platform.InputConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MessageScreen extends Screen {

    private EditBox textField;
    private Button sendButton;
    public static final ResourceLocation MESSAGE_CHANNEL =
            ResourceLocation.fromNamespaceAndPath("template_mod", "message");

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

            record MessagePayload(byte[] data) implements CustomPacketPayload {
                public static final CustomPacketPayload.Type<MessagePayload> TYPE =
                        new CustomPacketPayload.Type<>(
                                ResourceLocation.fromNamespaceAndPath("template_mod", "message"));

                public static final StreamCodec<ByteBuf, MessagePayload> STREAM_CODEC = StreamCodec.composite(
                        ByteBufCodecs.BYTE_ARRAY,
                        MessagePayload::data,
                        MessagePayload::new
                );

                @Override
                public @NotNull Type<? extends CustomPacketPayload> type() {
                    return TYPE;
                }
            }

            MessagePayload payload = new MessagePayload(bytes);
            ClientPlayNetworking.send(payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {  // Исправленная сигнатура
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);  // Исправленный метод
        super.render(guiGraphics, mouseX, mouseY, delta);

        // Рендерим текст поля ввода
        guiGraphics.drawString(this.font, "Message:", this.width / 2 - 100, this.height / 2 - 25, 0xFFFFFF);
    }

}
