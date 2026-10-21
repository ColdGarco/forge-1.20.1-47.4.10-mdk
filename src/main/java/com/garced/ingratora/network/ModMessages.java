package com.garced.ingratora.network;

import com.garced.ingratora.Ingratora;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Ingratora.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // Регистрируем наш пакет под уникальным ID
        net.messageBuilder(OpenInventoryPacket.class, nextId())
                .encoder(OpenInventoryPacket::encode)
                .decoder(OpenInventoryPacket::decode)
                .consumerNetworkThread(OpenInventoryPacket::handle)
                .add();
    }

    // Метод для отправки пакета с КЛИЕНТА на СЕРВЕР
    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}