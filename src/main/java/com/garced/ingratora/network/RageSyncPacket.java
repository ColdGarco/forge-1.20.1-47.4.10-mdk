package com.garced.ingratora.network;

import com.garced.ingratora.client.RageClientData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public record RageSyncPacket(int currentRage, int maxRage) {
    static void register(SimpleChannel channel, int packetId) {
        channel.registerMessage(
                packetId,
                RageSyncPacket.class,
                (packet, buffer) -> {
                    buffer.writeVarInt(packet.currentRage);
                    buffer.writeVarInt(packet.maxRage);
                },
                buffer -> new RageSyncPacket(buffer.readVarInt(), buffer.readVarInt()),
                RageSyncPacket::handle
        );
    }

    private static void handle(RageSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> RageClientData.setRage(packet.currentRage, packet.maxRage)
        ));
        context.setPacketHandled(true);
    }
}
