package com.garced.ingratora.client.renderer;

import com.garced.ingratora.client.model.AnimationDummyModel;
import com.garced.ingratora.common.entity.AnimationDummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AnimationDummyRenderer extends GeoEntityRenderer<AnimationDummyEntity> {
    public AnimationDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimationDummyModel());
    }
}
