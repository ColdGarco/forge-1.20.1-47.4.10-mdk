package com.garced.ingratora.global;

import com.garced.ingratora.Ingratora;

import com.garced.ingratora.common.player.CharacterClass;
import com.garced.ingratora.common.player.CharacterProfile;
import com.garced.ingratora.network.ModNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class Mana {
    public static final int MAX_MANA = 100;
    public static final int REGEN_AMOUNT = 5; // Amount of mana to regenerate per interval
    public static final int REGEN_INTERVAL_TICKS = 20; // 1 second in ticks (20 ticks = 1 second)

    private static final String MANA_TAG = Ingratora.MODID + "_mana";

    private Mana() {
    }
    // Получение текущей маны игрока. Если у игрока нет данных о мане, устанавливается максимальное значение.
    public static int getCurrentMana(Player player) {
        initialize(player);
        return player.getPersistentData().getInt(MANA_TAG);
    }

    public static boolean canUseMana(Player player) {
        return CharacterProfile.get(player).getCharacterClass().canUseMana();
    }

    public static boolean consume(Player player, int amount) {
        if (amount < 0 || !canUseMana(player) || getCurrentMana(player) < amount) {
            return false;
        }

        setCurrentMana(player, getCurrentMana(player) - amount);
        return true;
    }

    public static boolean regenerate(Player player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        int amount = characterClass.getManaRegenerationAmount();
        if (amount <= 0 || !characterClass.canUseMana()) {
            return false;
        }

        return restore(player, amount);
    }
    
    public static boolean restore(Player player, int amount) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        if (amount <= 0 || !characterClass.canUseMana()) {
            return false;
        }

        int currentMana = getCurrentMana(player);
        int updatedMana = Math.min(characterClass.getMaxMana(), currentMana + amount);
        if (updatedMana == currentMana) {
            return false;
        }

        setCurrentMana(player, updatedMana);
        return true;
    }
    // Синхронизация маны игрока с клиентом. Вызывается на сервере.
    public static void sync(ServerPlayer player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        ModNetworking.sendMana(
                player,
                getCurrentMana(player),
                characterClass.getMaxMana()
        );
    }
    // Инициализация маны игрока. Если у игрока нет данных о мане, устанавливается максимальное значение.
    public static void initialize(Player player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        if (!characterClass.canUseMana()) {
            player.getPersistentData().putInt(MANA_TAG, 0);
            return;
        }

        if (!player.getPersistentData().contains(MANA_TAG)) {
            player.getPersistentData().putInt(MANA_TAG, characterClass.getMaxMana());
            return;
        }

        int currentMana = player.getPersistentData().getInt(MANA_TAG);
        int boundedMana = Math.max(0, Math.min(characterClass.getMaxMana(), currentMana));
        player.getPersistentData().putInt(MANA_TAG, boundedMana);
    }
    // Копирование маны с одного игрока на другого. Используется при смене игрока (например, при смерти и возрождении).

    public static void onClassChosen(Player player) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        setCurrentMana(player, characterClass.canUseMana() ? characterClass.getMaxMana() : 0);
    }

    public static void copy(Player original, Player replacement) {
        int mana = getCurrentMana(original);
        replacement.getPersistentData().putInt(MANA_TAG, mana);
    }

    // Установка текущей маны игрока. Вызывается на сервере.
    private static void setCurrentMana(Player player, int mana) {
        CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
        int boundedMana = characterClass.canUseMana()
                ? Math.max(0, Math.min(characterClass.getMaxMana(), mana))
                : 0;
        player.getPersistentData().putInt(MANA_TAG, boundedMana);

        if (player instanceof ServerPlayer serverPlayer) {
            sync(serverPlayer);
        }
    }
}
