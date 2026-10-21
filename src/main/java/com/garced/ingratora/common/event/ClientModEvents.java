package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    // 1. Создаем саму переменную кнопки
    public static final KeyMapping INVENTORY_KEY = new KeyMapping(
            "key.ingratora.open_inventory", // Ключ названия кнопки
            GLFW.GLFW_KEY_C,                // Код клавиши (используем стандартную GLFW C)
            "key.categories.ingratora"      // Имя категории в меню настроек
    );

    // 2. Метод регистрации, который сейчас игра теряет
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(INVENTORY_KEY);
    }

    // 3. Ваш существующий метод настройки клиента
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
        net.minecraft.client.gui.screens.MenuScreens.register(
            com.garced.ingratora.common.init.MenuTypeReg.INVENTORY_MENU.get(), 
            com.garced.ingratora.client.hud.InventoryScreen::new
        );
    });
    }

}