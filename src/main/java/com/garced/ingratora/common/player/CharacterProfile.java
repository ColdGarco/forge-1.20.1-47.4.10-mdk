package com.garced.ingratora.common.player;

import com.garced.ingratora.Ingratora;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class CharacterProfile {
    private static final String PROFILE_TAG = Ingratora.MODID + "_character_profile";
    private static final String CLASS_TAG = "class";
    private static final String LEVEL_TAG = "level";
    private static final String EXPERIENCE_TAG = "experience";

    private final Player player;
    private CharacterClass characterClass;
    private int level;
    private long experience;

    private CharacterProfile(Player player, CompoundTag data) {
        this.player = player;
        this.characterClass = CharacterClass.getCharacterClass(data.getString(CLASS_TAG));
        this.level = Math.max(1, data.contains(LEVEL_TAG) ? data.getInt(LEVEL_TAG) : 1);
        this.experience = Math.max(0L, data.getLong(EXPERIENCE_TAG));
    }

    public static CharacterProfile get(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag profileData = playerData.getCompound(PROFILE_TAG);
        return new CharacterProfile(player, profileData);
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public boolean hasChosenClass() {
        return characterClass != CharacterClass.NONE;
    }

    public int getLevel() {
        return level;
    }

    public long getExperience() {
        return experience;
    }

    public boolean chooseClass(CharacterClass selectedClass) {
        if (hasChosenClass() || selectedClass == null || selectedClass == CharacterClass.NONE) {
            return false;
        }

        characterClass = selectedClass;
        save();
        return true;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
        save();
    }

    public void addExperience(long amount) {
        if (amount <= 0) {
            return;
        }

        experience = experience > Long.MAX_VALUE - amount
                ? Long.MAX_VALUE
                : experience + amount;
        save();
    }

    public void save() {
        CompoundTag profileData = new CompoundTag();
        profileData.putString(CLASS_TAG, characterClass.getId());
        profileData.putInt(LEVEL_TAG, level);
        profileData.putLong(EXPERIENCE_TAG, experience);
        player.getPersistentData().put(PROFILE_TAG, profileData);
    }

    public void copyTo(Player replacement) {
        replacement.getPersistentData().put(PROFILE_TAG,
                player.getPersistentData().getCompound(PROFILE_TAG).copy());
    }
}
