package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterProfile;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID)
public final class CharacterProfileEvents {
    private CharacterProfileEvents() {
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        CharacterProfile.get(event.getOriginal()).copyTo(event.getEntity());
    }
}
