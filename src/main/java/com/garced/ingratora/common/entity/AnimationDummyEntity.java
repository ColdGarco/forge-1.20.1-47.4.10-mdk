package com.garced.ingratora.common.entity;

import com.garced.ingratora.common.init.EntityReg;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AnimationDummyEntity extends PathfinderMob implements GeoEntity {
    private static final RawAnimation IDLE_ANIMATION =
            RawAnimation.begin().thenLoop("animation.animation_dummy.idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimationDummyEntity(EntityType<? extends AnimationDummyEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 0,
                state -> state.setAndContinue(IDLE_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
