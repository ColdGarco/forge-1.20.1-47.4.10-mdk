package com.garced.ingratora.common.player;

public enum CharacterClass {
    NONE ("unchosen", "None"),
    WARRIOR("warrior", "Warrior"),
    MAGE("mage", "Mage");

    private final String id;
    private final String displayName;

    CharacterClass(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
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
