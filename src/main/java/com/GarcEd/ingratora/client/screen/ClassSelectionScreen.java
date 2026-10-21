package com.garced.ingratora.client.screen;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.common.player.CharacterClass;
import com.garced.ingratora.network.ModNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ClassSelectionScreen extends Screen {
    private static final int IMAGE_SIZE = 64;
    private static final ResourceLocation WARRIOR_IMAGE =
            new ResourceLocation(Ingratora.MODID, "textures/gui/warrior_class_selection.png");
    private static final ResourceLocation MAGE_IMAGE =
            new ResourceLocation(Ingratora.MODID, "textures/gui/mage_class_selection.png");

    public ClassSelectionScreen() {
        super(Component.literal("Choose your class"));
    }

    @Override
    protected void init() {
        int buttonWidth = 120;
        int buttonHeight = 20;
        int left = width / 2 - buttonWidth - 5;
        int right = width / 2 + 5;
        int buttonY = height / 2 + 28;

        addRenderableWidget(Button.builder(
                Component.literal(CharacterClass.WARRIOR.getDisplayName()),
                button -> choose(CharacterClass.WARRIOR)
        ).bounds(left, buttonY, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(
                Component.literal(CharacterClass.MAGE.getDisplayName()),
                button -> choose(CharacterClass.MAGE)
        ).bounds(right, buttonY, buttonWidth, buttonHeight).build());
    }

    private void choose(CharacterClass characterClass) {
        ModNetworking.sendClassChoice(characterClass);
    }

    @Override
    public void onClose() {
        // Keep the required class choice screen open until a class is selected.
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int buttonWidth = 120;
        int leftButtonX = width / 2 - buttonWidth - 5;
        int rightButtonX = width / 2 + 5;
        int imageY = height / 2 + 28 - IMAGE_SIZE - 12;

        graphics.blit(
                WARRIOR_IMAGE,
                leftButtonX + (buttonWidth - IMAGE_SIZE) / 2,
                imageY,
                0,
                0,
                IMAGE_SIZE,
                IMAGE_SIZE,
                IMAGE_SIZE,
                IMAGE_SIZE
        );
        graphics.blit(
                MAGE_IMAGE,
                rightButtonX + (buttonWidth - IMAGE_SIZE) / 2,
                imageY,
                0,
                0,
                IMAGE_SIZE,
                IMAGE_SIZE,
                IMAGE_SIZE,
                IMAGE_SIZE
        );
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
