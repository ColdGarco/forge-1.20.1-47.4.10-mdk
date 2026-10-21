package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterClass;
import com.garced.ingratora.common.player.CharacterProfile;
import com.garced.ingratora.global.Rage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID)
public final class RageEvents {
    private RageEvents() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Rage.initialize(player);
            Rage.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Rage.copy(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Rage.initialize(player);
            Rage.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
                && event.player instanceof ServerPlayer player
                && !player.isRemoved()) {
            CharacterClass characterClass = CharacterProfile.get(player).getCharacterClass();
            int interval = characterClass.getRageRegenerationIntervalTicks();
            if (characterClass.canUseRage()
                    && interval > 0
                    && player.tickCount % interval == 0) {
                Rage.regenerate(player);
            }
        }
    }
}
