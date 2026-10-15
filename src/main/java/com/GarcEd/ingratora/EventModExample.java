package com.GarcEd.ingratora;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraft.world.entity.player.Player;

@EventBusSubscriber(modid = Ingratora.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class EventModExample {
    @SubscribeEvent 
    public static void OnEvent(Player player) {
        }
}