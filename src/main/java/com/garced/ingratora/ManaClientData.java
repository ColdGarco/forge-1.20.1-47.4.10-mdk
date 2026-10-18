package com.garced.ingratora;

public final class ManaClientData {
    private static int currentMana = Mana.MAX_MANA;

    private ManaClientData() {
    }

    public static int getCurrentMana() {
        return currentMana;
    }

    public static void setCurrentMana(int mana) {
        currentMana = Math.max(0, Math.min(Mana.MAX_MANA, mana));
    }
}
