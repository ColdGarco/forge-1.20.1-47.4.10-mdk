package com.garced.ingratora;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ManaOverlayGUI{
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
        int maxMana = Mana.MAX_MANA;
        int currentMana = ManaClientData.getCurrentMana();

        int x = 10;
        int y = screenHeight - 10;
        int barWidth = 100;
        int filledWidth = barWidth * currentMana / maxMana;

        graphics.fill(x, y, x + barWidth, y + 6, 0xFF333333);
        graphics.fill(x, y, x + filledWidth, y + 6, 0xFF3399FF);
        graphics.drawString(
                gui.getMinecraft().font,
                "Mana: " + currentMana + "/" + maxMana,
                x,
                y - 10,
                0xFFFFFFFF
        );
    }
}

    