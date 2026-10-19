package com.garced.ingratora.global;

import com.garced.ingratora.Ingratora;

import com.garced.ingratora.network.ManaNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class Mana {
    public static final int MAX_MANA = 100;
    public static final int REGEN_AMOUNT = 5;
    public static final int REGEN_INTERVAL_TICKS = 20;

    private static final String MANA_TAG = Ingratora.MODID + "_mana";

    private Mana() {
    }

    public static int getCurrentMana(Player player) {
        initialize(player);
        return player.getPersistentData().getInt(MANA_TAG);
    }

    public static boolean consume(Player player, int amount) {
        if (amount < 0 || getCurrentMana(player) < amount) {
            return false;
        }

        setCurrentMana(player, getCurrentMana(player) - amount);
        return true;
    }

    public static boolean regenerate(Player player, int amount) {
        if (amount <= 0) {
            return false;
        }

        int currentMana = getCurrentMana(player);
        int updatedMana = Math.min(MAX_MANA, currentMana + amount);
        if (updatedMana == currentMana) {
            return false;
        }

        setCurrentMana(player, updatedMana);
        return true;
    }

    public static void sync(ServerPlayer player) {
        ManaNetwork.sendToPlayer(player, getCurrentMana(player));
    }

    public static void initialize(Player player) {
        if (!player.getPersistentData().contains(MANA_TAG)) {
            player.getPersistentData().putInt(MANA_TAG, MAX_MANA);
            return;
        }

        int currentMana = player.getPersistentData().getInt(MANA_TAG);
        int boundedMana = Math.max(0, Math.min(MAX_MANA, currentMana));
        player.getPersistentData().putInt(MANA_TAG, boundedMana);
    }

    public static void copy(Player original, Player replacement) {
        int mana = getCurrentMana(original);
        replacement.getPersistentData().putInt(MANA_TAG, mana);
    }

    private static void setCurrentMana(Player player, int mana) {
        int boundedMana = Math.max(0, Math.min(MAX_MANA, mana));
        player.getPersistentData().putInt(MANA_TAG, boundedMana);

        if (player instanceof ServerPlayer serverPlayer) {
            sync(serverPlayer);
        }
    }
}
