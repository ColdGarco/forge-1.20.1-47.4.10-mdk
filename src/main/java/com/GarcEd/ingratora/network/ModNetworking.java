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
    static int packetID = 0;
    public static int nextID(){
        return packetID++;
    }

    @SuppressWarnings("removal")
    static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Ingratora.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

    private ModNetworking() {
    }

    public static void register() {
        ManaSyncPacket.register(CHANNEL, nextID());
        CharacterClassPackets.register(CHANNEL, ModNetworking::nextID);
        RageSyncPacket.register(CHANNEL, nextID());
        OpenInventoryPacket.register(CHANNEL, nextID());
    }

    public static void sendMana(ServerPlayer player, int currentMana, int maxMana) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ManaSyncPacket(currentMana, maxMana)
        );
    }

    public static void sendRage(ServerPlayer player, int currentRage, int maxRage) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new RageSyncPacket(currentRage, maxRage)
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

    public static void sendOpenInventory() {
        CHANNEL.sendToServer(new OpenInventoryPacket());
    }

    public static void syncCharacterClass(ServerPlayer player, CharacterClass characterClass) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new CharacterClassPackets.CharacterClassSyncS2C(characterClass.getId())
        );
    }
}
