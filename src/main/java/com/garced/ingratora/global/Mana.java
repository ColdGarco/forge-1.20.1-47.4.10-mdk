package com.garced.ingratora.global;

import com.garced.ingratora.Ingratora;

import com.garced.ingratora.network.ManaNetwork;
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
    // Установка текущей маны игрока. Возвращает true, если установка прошла успешно, иначе false.
     private static void setCurrentMana(Player player, int mana) {
        int boundedMana = Math.max(0, Math.min(MAX_MANA, mana));
        player.getPersistentData().putInt(MANA_TAG, boundedMana);

        if (player instanceof ServerPlayer serverPlayer) {
            sync(serverPlayer);
        }
    }
    // Потребление маны игроком. Возвращает true, если потребление прошло успешно, иначе false.
    public static boolean consume(Player player, int amount) {
        if (amount < 0 || getCurrentMana(player) < amount) {
            return false;
        }

        setCurrentMana(player, getCurrentMana(player) - amount);
        return true;
    }

    // Восстановление маны игроку. Возвращает true, если восстановление прошло успешно, иначе false.
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
    // Синхронизация маны игрока с клиентом. Вызывается на сервере.
    public static void sync(ServerPlayer player) {
        ManaNetwork.sendToPlayer(player, getCurrentMana(player));
    }
    // Инициализация маны игрока. Если у игрока нет данных о мане, устанавливается максимальное значение.
    public static void initialize(Player player) {
        if (!player.getPersistentData().contains(MANA_TAG)) {
            player.getPersistentData().putInt(MANA_TAG, MAX_MANA);
            return;
        }

        int currentMana = player.getPersistentData().getInt(MANA_TAG);
        int boundedMana = Math.max(0, Math.min(MAX_MANA, currentMana));
        player.getPersistentData().putInt(MANA_TAG, boundedMana);
    }
    // Копирование маны с одного игрока на другого. Используется при смене игрока (например, при смерти и возрождении).
    public static void copy(Player original, Player replacement) {
        int mana = getCurrentMana(original);
        replacement.getPersistentData().putInt(MANA_TAG, mana);
    }
    // Установка текущей маны игрока. Вызывается на сервере.
   
}
