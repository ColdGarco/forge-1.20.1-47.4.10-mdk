package com.garced.ingratora.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Quaternionf;

public class InventoryScreen extends AbstractContainerScreen<InventoryMenu> {
    
    // В будущем здесь будет ваша кастомная рамка интерфейса (сетка, правая панель описания)
    // Пока мы рисуем полупрозрачный темный задний фон кодом, чтобы мир сзади красиво просвечивал
    private static final ResourceLocation HUD_ELEMENTS = 
            new ResourceLocation("ingratora", "textures/gui/rpg_hud_elements.png");

    public InventoryScreen(InventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        // Отключаем жесткие ванильные рамки, так как интерфейс полноэкранный
        this.imageWidth = this.width;
        this.imageHeight = this.height;
    }

    @Override
    protected void init() {
    // 1. Устанавливаем полноэкранные размеры интерфейса
    this.imageWidth = this.width;
    this.imageHeight = this.height;
    super.init();

    // Прячем стандартный ванильный текст
    this.titleLabelX = 9999;
    this.inventoryLabelX = 9999;

    // 2. Вместо создания нового меню просто приказываем старому пересчитать координаты слотов!
    if (this.menu != null) {
        this.menu.updateSlotPositions(this.width, this.height);
    }
}

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. Рисуем глубокое RPG-затемнение заднего фона вместо дефолтного ванильного
        // Градиент от темно-серого к почти черному с прозрачностью
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0xC0101010, 0xD00A0A0A);
        
        // 2. Рендерим слоты и подсказки предметов
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY); 
    }

    @Override
protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    
    int centerX = this.width / 2;
    int centerY = this.height / 2;

    // =========================================================================
    // ВСТАВЛЯЕМ СЮДА: ОТРИСОВКА СЕТКИ И ЯЧЕЕК ДЛЯ ВСЕХ СЛОТОВ
    // =========================================================================
    if (this.menu != null) {
        for (net.minecraft.world.inventory.Slot slot : this.menu.slots) {
            // Вычисляем экранные координаты каждого слота
            int slotX = this.leftPos + slot.x;
            int slotY = this.topPos + slot.y;

            // Рисуем аккуратную темную RPG-рамку вокруг слота (размер слота в MC 16x16 пикселей)
            // fillGradient нарисует полупрозрачный квадрат с легкой рамкой
            guiGraphics.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0x40FFFFFF); // Внешний светлый контур
            guiGraphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF202020);        // Темный фон ячейки
        }
    }

    // РЕНДЕРИМ 3D ПЕРСОНАЖА (Оставляем как было)
    if (this.minecraft.player != null) {
        int modelX = centerX + 40; 
        int modelY = centerY + 80;
        int modelScale = 90; 

        renderStaticEntity(guiGraphics, modelX, modelY, modelScale, this.minecraft.player);
    }
    }

    /**
     * Модифицированный метод рендеринга: персонаж зафиксирован лицом к игроку
     */
    private static void renderStaticEntity(GuiGraphics guiGraphics, int x, int y, int scale, LivingEntity entity) {
        guiGraphics.pose().pushPose();
        // Смещаем модельку в пространстве интерфейса
        guiGraphics.pose().translate((double)x, (double)y, 50.0D);
        guiGraphics.pose().scale((float)(-scale), (float)scale, (float)scale);
        
        // Разворачиваем модельку лицом (на 180 градусов вокруг оси Z)
        Quaternionf quaternionf = (new Quaternionf()).rotationZ((float)Math.PI);
        guiGraphics.pose().mulPose(quaternionf);

        // Сохраняем реальные повороты головы игрока из мира
        float f = entity.yBodyRot;
        float f1 = entity.getYRot();
        float f2 = entity.getXRot();
        float f3 = entity.yHeadRotO;
        float f4 = entity.yHeadRot;

        // Насильно выставляем углы в 0. 
        // В комбинации с разворотом выше — персонаж встанет идеально лицом к экрану
        entity.yBodyRot = 45.0F; 
        entity.setYRot(-45.0F);
        entity.setXRot(0.0F);
        entity.yHeadRot = 0.0F;
        entity.yHeadRotO = 0.0F;

        // Рендерим сущность без теней под ногами
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadow(false);
        
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880);
        });
        
        guiGraphics.bufferSource().endBatch();
        entityrenderdispatcher.setRenderShadow(true);

        // Возвращаем углы назад, чтобы не ломать камеру в третьем лице
        entity.yBodyRot = f;
        entity.setYRot(f1);
        entity.setXRot(f2);
        entity.yHeadRotO = f3;
        entity.yHeadRot = f4;
        
        guiGraphics.pose().popPose();
    }
}