package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterProfile;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.garced.ingratora.common.player.CharacterAccessoriesProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CharacterProfileEvents {
    private CharacterProfileEvents() {
    }
    
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        CharacterProfile.get(event.getOriginal()).copyTo(event.getEntity());
    }

    // 
    @SubscribeEvent // Привязываем скрытый контейнер к игроку при его создании
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof Player) {
        // Привязываем скрытый контейнер к игроку под именем вашего мода
        event.addCapability(new ResourceLocation(Ingratora.MODID, "accessories"), new CharacterAccessoriesProvider());
    }
    }
}



