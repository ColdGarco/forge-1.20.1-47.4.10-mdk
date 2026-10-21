package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterClass;
import com.garced.ingratora.common.player.CharacterProfile;
import com.garced.ingratora.network.ModNetworking;
import net.minecraft.server.level.ServerPlayer;
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
            event.addCapability(
                    new ResourceLocation(Ingratora.MODID, "accessories"),
                    new CharacterAccessoriesProvider()
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CharacterProfile profile = CharacterProfile.get(player);
            if (profile.hasChosenClass()) {
                ModNetworking.syncCharacterClass(player, profile.getCharacterClass());
            } else {
                ModNetworking.openClassSelection(player);
            }
        }
    }
}


