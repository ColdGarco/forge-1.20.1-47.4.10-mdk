package com.garced.ingratora.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class ModEntity extends Entity {
    protected ModEntity(EntityType<? extends ModEntity> entityType, Level level) {
        super(entityType, level);
    }
}
