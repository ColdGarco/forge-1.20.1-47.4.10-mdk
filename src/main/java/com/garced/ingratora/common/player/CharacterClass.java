package com.garced.ingratora.common.player;

public enum CharacterClass {
    NONE("unchosen", "None", false, 0, 0, 0),
    WARRIOR("warrior", "Warrior", false, 0, 0, 0),
    MAGE("mage", "Mage", true, 100, 5, 20);

    private final String id;
    private final String displayName;
    private final boolean manaEnabled;
    private final int maxMana;
    private final int manaRegenerationAmount;
    private final int manaRegenerationIntervalTicks;

    CharacterClass(
            String id,
            String displayName,
            boolean manaEnabled,
            int maxMana,
            int manaRegenerationAmount,
            int manaRegenerationIntervalTicks
    ) {
        this.id = id;
        this.displayName = displayName;
        this.manaEnabled = manaEnabled;
        this.maxMana = maxMana;
        this.manaRegenerationAmount = manaRegenerationAmount;
        this.manaRegenerationIntervalTicks = manaRegenerationIntervalTicks;
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

    public static CharacterClass getCharacterClass(String id) {
        for (CharacterClass characterClass : values()) {
            if (characterClass.id.equals(id)) {
                return characterClass;
            }
        }
        return NONE;
    }
}
