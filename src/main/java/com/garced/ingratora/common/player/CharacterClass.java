package com.garced.ingratora.common.player;

public enum CharacterClass {
    NONE("unchosen", "None", false, false, 0, 0, 0, 0, 0, 0),
    WARRIOR("warrior", "Warrior", false, true, 0, 0, 0, 100, 5, 20),
    MAGE("mage", "Mage", true, false, 100, 5, 20, 0, 0, 0);

    private final String id;
    private final String displayName;
    private final boolean manaEnabled;
    private final boolean rageEnabled;
    private final int maxMana;
    private final int manaRegenerationAmount;
    private final int manaRegenerationIntervalTicks;
    private final int maxRage;
    private final int rageRegenerationAmount;
    private final int rageRegenerationIntervalTicks;


    CharacterClass(
            String id,
            String displayName,
            boolean manaEnabled,
            boolean rageEnabled,
            int maxMana,
            int manaRegenerationAmount,
            int manaRegenerationIntervalTicks,
            int maxRage,
            int rageRegenerationAmount,
            int rageRegenerationIntervalTicks
    ) 
    {
        this.id = id;
        this.displayName = displayName;
        this.manaEnabled = manaEnabled;
        this.maxMana = maxMana;
        this.manaRegenerationAmount = manaRegenerationAmount;
        this.manaRegenerationIntervalTicks = manaRegenerationIntervalTicks;
        this.rageEnabled = rageEnabled;
        this.maxRage = maxRage;
        this.rageRegenerationAmount = rageRegenerationAmount;
        this.rageRegenerationIntervalTicks = rageRegenerationIntervalTicks;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canUseMana() {
        return manaEnabled;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getManaRegenerationAmount() {
        return manaRegenerationAmount;
    }

    public int getManaRegenerationIntervalTicks() {
        return manaRegenerationIntervalTicks;
    }

    public boolean canUseRage() {
        return rageEnabled;
    }

    public int getMaxRage() {
        return maxRage;
    }

    public int getRageRegenerationAmount() {
        return rageRegenerationAmount;
    }

    public int getRageRegenerationIntervalTicks() {
        return rageRegenerationIntervalTicks;
    }

    public static CharacterClass getCharacterClass(String id) {
        for (CharacterClass characterClass : values()) {
            if (characterClass.id.equals(id)) {
                return characterClass;
            }
        }
        return NONE;
    }
}
