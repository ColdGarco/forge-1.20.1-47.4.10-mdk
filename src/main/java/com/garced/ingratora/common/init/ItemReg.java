package com.garced.ingratora.common.init;

import com.garced.ingratora.Ingratora;
import net.minecraft.world.item.Item;
import com.garced.ingratora.common.spell.FireballScrollItem;
import com.garced.ingratora.common.spell.LightBeamScrollItem;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemReg {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ingratora.MODID);

        public static final RegistryObject<Item> FIRE_BALL_SCROLL = ITEMS.register("fireball_scroll", 
                () -> new FireballScrollItem(new Item.Properties()));
        public static final RegistryObject<Item> LIGHT_BEAM_SCROLL = ITEMS.register("lightbeam_scroll", 
            () -> new LightBeamScrollItem(new Item.Properties()));

        public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        }

}
