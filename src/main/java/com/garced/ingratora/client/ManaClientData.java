package com.garced.ingratora.client;
public final class ManaClientData {
    private static int currentMana;
    private static int maxMana;

    private ManaClientData() {
    }

    public static int getCurrentMana() {
        return currentMana;
    }

    public static int getMaxMana() {
        return maxMana;
    }

    public static void setMana(int mana, int maximum) {
        maxMana = Math.max(0, maximum);
        currentMana = Math.max(0, Math.min(maxMana, mana));
    }
}
