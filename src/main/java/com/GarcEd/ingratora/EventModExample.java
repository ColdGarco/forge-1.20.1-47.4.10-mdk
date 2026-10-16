package com.GarcEd.ingratora;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventModExample {

    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        // Check if the entity jumping is a player
        if (event.getEntity() instanceof Player player) {
            // Check that this is running on the server side
            if (!player.level().isClientSide()) {
                // Sends the message directly to the player's in-game chat
                player.sendSystemMessage(
                    Component.literal(">>> SUCCESS: " + player.getName().getString() + " jumped! Event is working! <<<")
                );
            }
        }
    }
}