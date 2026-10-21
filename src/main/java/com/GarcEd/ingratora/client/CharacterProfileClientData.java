package com.garced.ingratora.client;

import com.garced.ingratora.common.player.CharacterClass;

public final class CharacterProfileClientData {
    private static CharacterClass characterClass = CharacterClass.NONE;

    private CharacterProfileClientData() {
    }

    public static CharacterClass getCharacterClass() {
        return characterClass;
    }

    public static void setCharacterClass(CharacterClass selectedClass) {
        characterClass = selectedClass;
    }
}
