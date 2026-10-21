package com.garced.ingratora.client;

public final class RageClientData {
    private static int currentRage;
    private static int maxRage;

    private RageClientData() {
    }

    public static int getCurrentRage() {
        return currentRage;
    }

    public static int getMaxRage() {
        return maxRage;
    }

    public static void setRage(int rage, int maximum) {
        maxRage = Math.max(0, maximum);
        currentRage = Math.max(0, Math.min(maxRage, rage));
    }
}