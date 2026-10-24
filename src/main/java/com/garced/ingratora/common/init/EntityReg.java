package com.garced.ingratora.common.init;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.entity.AnimationDummyEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EntityReg {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Ingratora.MODID);

    public static final RegistryObject<EntityType<AnimationDummyEntity>> ANIMATION_DUMMY =
            ENTITY_TYPES.register("animation_dummy", () ->
                    EntityType.Builder.of(AnimationDummyEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F)
                            .clientTrackingRange(8)
                            .build("animation_dummy"));

    private EntityReg() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
