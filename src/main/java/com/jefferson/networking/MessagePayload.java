package com.jefferson.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record MessagePayload(byte[] data) implements CustomPacketPayload {
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
