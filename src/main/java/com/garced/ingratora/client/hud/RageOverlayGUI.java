package com.garced.ingratora.client.hud;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.client.CharacterProfileClientData;
import com.garced.ingratora.client.RageClientData;
import com.garced.ingratora.common.player.CharacterClass;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RageOverlayGUI {
    private static final ResourceLocation RAGE_ICONS =
            new ResourceLocation(Ingratora.MODID, "textures/gui/rageiconmedium.png");

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(
                VanillaGuiOverlay.HOTBAR.id(),
                "rage_bar",
                RageOverlayGUI::render
        );
    }

    private static void render(
            ForgeGui gui,
            GuiGraphics graphics,
            float partialTick,
            int screenWidth,
            int screenHeight
    ) {
        CharacterClass characterClass = CharacterProfileClientData.getCharacterClass();
        if (!characterClass.canUseRage()) {
            return;
        }

        int currentRage = RageClientData.getCurrentRage();
        int maxRage = RageClientData.getMaxRage();
        int iconCount = (maxRage + 9) / 10;
        int x = screenWidth / 2 + 10;
        int y = screenHeight - 50;

        for (int i = 0; i < iconCount; i++) {
            int rageForIcon = Math.max(0, Math.min(10, currentRage - i * 10));
            int textureU = rageForIcon >= 10 ? 0 : rageForIcon >= 5 ? 8 : 15;

            graphics.blit(
                    RAGE_ICONS,
                    x + i * 8,
                    y,
                    textureU,
                    0,
                    7,
                    9,
                    22,
                    9
            );
        }
    }
}

    