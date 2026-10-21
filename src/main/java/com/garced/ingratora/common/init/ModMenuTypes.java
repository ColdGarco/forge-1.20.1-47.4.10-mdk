package com.garced.ingratora.common.init;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.client.hud.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    // Создаем регистратор типов меню для вашего ID мода
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Ingratora.MODID);

    // Регистрируем наше меню
    public static final RegistryObject<MenuType<InventoryMenu>> INVENTORY_MENU = 
            MENUS.register("inventory_menu", 
                    () -> IForgeMenuType.create(InventoryMenu::new));

    // Этот метод мы вызовем в главном классе мода
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
