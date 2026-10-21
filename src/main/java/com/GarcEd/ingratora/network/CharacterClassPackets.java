package com.garced.ingratora.network;

import com.garced.ingratora.client.CharacterProfileClientData;
import com.garced.ingratora.client.screen.ClassSelectionScreen;
import com.garced.ingratora.common.player.CharacterClass;
import com.garced.ingratora.common.player.CharacterProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public final class CharacterClassPackets {
    private CharacterClassPackets() {
    }

    static void register(SimpleChannel channel, int firstPacketId) {
        channel.registerMessage(
                firstPacketId,
                OpenSelectionS2C.class,
                (packet, buffer) -> {
                },
                buffer -> new OpenSelectionS2C(),
                OpenSelectionS2C::handle
        );
        channel.registerMessage(
                firstPacketId + 1,
                SelectClassC2S.class,
                (packet, buffer) -> buffer.writeUtf(packet.classId),
                buffer -> new SelectClassC2S(buffer.readUtf(32)),
                SelectClassC2S::handle
        );
        channel.registerMessage(
                firstPacketId + 2,
                CharacterClassSyncS2C.class,
                (packet, buffer) -> buffer.writeUtf(packet.classId),
                buffer -> new CharacterClassSyncS2C(buffer.readUtf(32)),
                CharacterClassSyncS2C::handle
        );
    }

    public record OpenSelectionS2C() {
        private static void handle(
                OpenSelectionS2C packet,
                Supplier<NetworkEvent.Context> contextSupplier
        ) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                    Dist.CLIENT,
                    () -> () -> {
                        CharacterProfileClientData.setCharacterClass(CharacterClass.NONE);
                        Minecraft.getInstance().setScreen(new ClassSelectionScreen());
                    }
            ));
            context.setPacketHandled(true);
        }
    }

    public record SelectClassC2S(String classId) {
        private static void handle(
                SelectClassC2S packet,
                Supplier<NetworkEvent.Context> contextSupplier
        ) {
            NetworkEvent.Context context = contextSupplier.get();
            ServerPlayer player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> {
                    CharacterProfile profile = CharacterProfile.get(player);
                    CharacterClass selectedClass = CharacterClass.getCharacterClass(packet.classId);
                    if (profile.chooseClass(selectedClass)) {
                        ModNetworking.syncCharacterClass(player, selectedClass);
                    } else if (profile.hasChosenClass()) {
                        ModNetworking.syncCharacterClass(player, profile.getCharacterClass());
                    } else {
                        ModNetworking.openClassSelection(player);
                    }
                });
            }
            context.setPacketHandled(true);
        }
    }

    public record CharacterClassSyncS2C(String classId) {
        private static void handle(
                CharacterClassSyncS2C packet,
                Supplier<NetworkEvent.Context> contextSupplier
        ) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                    Dist.CLIENT,
                    () -> () -> {
                        CharacterClass selectedClass = CharacterClass.getCharacterClass(packet.classId);
                        CharacterProfileClientData.setCharacterClass(selectedClass);
                        Minecraft.getInstance().setScreen(null);
                    }
            ));
            context.setPacketHandled(true);
        }
    }
}
