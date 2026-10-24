package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.entity.AnimationDummyEntity;
import com.garced.ingratora.common.init.EntityReg;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityAttributes {
    private ModEntityAttributes() {
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityReg.ANIMATION_DUMMY.get(), AnimationDummyEntity.createAttributes().build());
    }
}
