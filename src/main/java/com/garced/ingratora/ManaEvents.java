package com.garced.ingratora;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID)
public final class ManaEvents {
    private ManaEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Mana.initialize(player);
            Mana.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Mana.copy(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Mana.initialize(player);
            Mana.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
                && event.player instanceof ServerPlayer player
                && !player.isRemoved()
                && player.tickCount % Mana.REGEN_INTERVAL_TICKS == 0) {
            Mana.regenerate(player, Mana.REGEN_AMOUNT);
        }
    }
}
