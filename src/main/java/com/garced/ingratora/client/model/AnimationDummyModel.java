package com.garced.ingratora.client.model;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.entity.AnimationDummyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AnimationDummyModel extends GeoModel<AnimationDummyEntity> {
    private static final ResourceLocation MODEL =
            new ResourceLocation(Ingratora.MODID, "geo/animation_dummy.geo.json");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Ingratora.MODID, "textures/entity/animation_dummy.png");
    private static final ResourceLocation ANIMATIONS =
            new ResourceLocation(Ingratora.MODID, "animations/animation_dummy.animation.json");

    @Override
    public ResourceLocation getModelResource(AnimationDummyEntity entity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AnimationDummyEntity entity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AnimationDummyEntity entity) {
        return ANIMATIONS;
    }
}
