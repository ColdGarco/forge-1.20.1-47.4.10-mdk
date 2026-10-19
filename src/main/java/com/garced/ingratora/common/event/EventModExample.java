package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventModExample {
    static int i = 0;
    
    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                switch(i){
                    case 0:
                        player.sendSystemMessage(Component.literal("Кто"));
                        break;
                    case 1:
                        player.sendSystemMessage(Component.literal("не"));
                        break;
                    case 2:
                        player.sendSystemMessage(Component.literal("скачет"));
                        break;
                    case 3:
                        player.sendSystemMessage(Component.literal("тот"));
                        break;
                    case 4:
                        player.sendSystemMessage(Component.literal("лох"));
                        break;
                }
                
                if (i == 4) {
                    i = 0;
                } else {
                    i++;
                }
            }
        }
    }
}