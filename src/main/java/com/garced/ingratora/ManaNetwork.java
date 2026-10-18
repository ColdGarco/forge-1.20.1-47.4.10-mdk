package com.garced.ingratora;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public final class ManaNetwork {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Ingratora.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId;

    private ManaNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(
                packetId++,
                ManaSyncPacket.class,
                (packet, buffer) -> buffer.writeVarInt(packet.currentMana),
                buffer -> new ManaSyncPacket(buffer.readVarInt()),
                ManaNetwork::handleManaSync
        );
    }

    public static void sendToPlayer(ServerPlayer player, int currentMana) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ManaSyncPacket(currentMana)
        );
    }

    private static void handleManaSync(ManaSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() ->
                net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
                        net.minecraftforge.api.distmarker.Dist.CLIENT,
                        () -> () -> ManaClientData.setCurrentMana(packet.currentMana)
                )
        );
        context.setPacketHandled(true);
    }

    private static final class ManaSyncPacket {
        private final int currentMana;

        private ManaSyncPacket(int currentMana) {
            this.currentMana = currentMana;
        }
    }
}
