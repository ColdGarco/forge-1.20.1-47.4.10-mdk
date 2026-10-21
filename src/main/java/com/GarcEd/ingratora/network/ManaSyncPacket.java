package com.garced.ingratora.network;

import com.garced.ingratora.client.ManaClientData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public record ManaSyncPacket(int currentMana, int maxMana) {
    static void register(SimpleChannel channel, int packetId) {
        channel.registerMessage(
                packetId,
                ManaSyncPacket.class,
                (packet, buffer) -> {
                    buffer.writeVarInt(packet.currentMana);
                    buffer.writeVarInt(packet.maxMana);
                },
                buffer -> new ManaSyncPacket(buffer.readVarInt(), buffer.readVarInt()),
                ManaSyncPacket::handle
        );
    }

    private static void handle(ManaSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> ManaClientData.setMana(packet.currentMana, packet.maxMana)
        ));
        context.setPacketHandled(true);
    }
}
