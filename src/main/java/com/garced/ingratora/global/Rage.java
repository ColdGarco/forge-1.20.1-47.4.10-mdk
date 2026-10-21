package com.garced.ingratora.global;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterClass;
import com.garced.ingratora.common.player.CharacterProfile;
import com.garced.ingratora.network.ModNetworking;
import com.garced.ingratora.network.RageSyncPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class Rage {
    private static final String RAGE_TAG = Ingratora.MODID + "_rage";

    private Rage() {
    }

    public static int getCurrentRage(Player player) {
        initialize(player);
        return player.getPersistentData().getInt(RAGE_TAG);
    }

    public static boolean canUseRage(Player player) {
        return CharacterProfile.get(player).getCharacterClass().canUseRage();
    }

    public static boolean consume(Player player, int amount) {
        if (amount < 0 || !canUseRage(player) || getCurrentRage(player) < amount) {
            return false;
        }

        setCurrentRage(player, getCurrentRage(player) - amount);
        return true;
    }

    public static boolean restore(Player player, int amount) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        if (amount <= 0 || !characterClass.canUseRage()) {
            return false;
        }

        int currentRage = getCurrentRage(player);
        int updatedRage = Math.min(characterClass.getMaxRage(), currentRage + amount);
        if (updatedRage == currentRage) {
            return false;
        }

        setCurrentRage(player, updatedRage);
        return true;
    }

    public static boolean regenerate(Player player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        return restore(player, characterClass.getRageRegenerationAmount());
    }

    public static void initialize(Player player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        if (!characterClass.canUseRage()) {
            player.getPersistentData().putInt(RAGE_TAG, 0);
            return;
        }

        if (!player.getPersistentData().contains(RAGE_TAG)) {
            player.getPersistentData().putInt(RAGE_TAG, characterClass.getMaxRage());
            return;
        }

        int currentRage = player.getPersistentData().getInt(RAGE_TAG);
        player.getPersistentData().putInt(RAGE_TAG, clamp(currentRage, characterClass.getMaxRage()));
    }

    public static void sync(ServerPlayer player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        ModNetworking.sendRage(
                player,
                getCurrentRage(player),
                characterClass.getMaxRage()
        );
    }

    public static void copy(Player original, Player replacement) {
        replacement.getPersistentData().putInt(
                RAGE_TAG,
                getCurrentRage(original)
        );
    }

    public static void onClassChosen(Player player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        setCurrentRage(player, characterClass.canUseRage() ? characterClass.getMaxRage() : 0);
    }

    private static void setCurrentRage(Player player, int rage) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        int boundedRage = characterClass.canUseRage()
                ? clamp(rage, characterClass.getMaxRage())
                : 0;
        player.getPersistentData().putInt(RAGE_TAG, boundedRage);

        if (player instanceof ServerPlayer serverPlayer) {
            sync(serverPlayer);
        }
    }

    private static int clamp(int rage, int maximum) {
        return Math.max(0, Math.min(maximum, rage));
    }
}
