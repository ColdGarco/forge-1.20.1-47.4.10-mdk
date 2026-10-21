package com.garced.ingratora.network;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";

    @SuppressWarnings("removal")
    static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Ingratora.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private ModNetworking() {
    }

    public static void register() {
        ManaSyncPacket.register(CHANNEL, 0);
        CharacterClassPackets.register(CHANNEL, 1);
    }

    public static void sendMana(ServerPlayer player, int currentMana) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ManaSyncPacket(currentMana)
        );
    }

    public static void openClassSelection(ServerPlayer player) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new CharacterClassPackets.OpenSelectionS2C()
        );
    }

    public static void sendClassChoice(CharacterClass characterClass) {
        CHANNEL.sendToServer(
                new CharacterClassPackets.SelectClassC2S(characterClass.getId())
        );
    }

    public static void syncCharacterClass(ServerPlayer player, CharacterClass characterClass) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new CharacterClassPackets.CharacterClassSyncS2C(characterClass.getId())
        );
    }
}
