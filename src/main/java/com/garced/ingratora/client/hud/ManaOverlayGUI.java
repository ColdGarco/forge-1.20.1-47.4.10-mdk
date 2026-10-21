package com.garced.ingratora.client.hud;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.client.CharacterProfileClientData;
import com.garced.ingratora.client.ManaClientData;
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
public class ManaOverlayGUI {
    private static final ResourceLocation MANA_ICONS =
            new ResourceLocation(Ingratora.MODID, "textures/gui/manabarmedium.png");

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(
                VanillaGuiOverlay.HOTBAR.id(),
                "mana_bar",
                ManaOverlayGUI::render
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
        if (!characterClass.canUseMana()) {
            return;
        }

        int currentMana = ManaClientData.getCurrentMana();
        int maxMana = ManaClientData.getMaxMana();
        int iconCount = (maxMana + 9) / 10;
        int x = screenWidth / 2 + 10;
        int y = screenHeight - 50;

        for (int i = 0; i < iconCount; i++) {
            int manaForIcon = Math.max(0, Math.min(10, currentMana - i * 10));
            int textureU = manaForIcon >= 10 ? 0 : manaForIcon >= 5 ? 8 : 15;

            graphics.blit(
                    MANA_ICONS,
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

    