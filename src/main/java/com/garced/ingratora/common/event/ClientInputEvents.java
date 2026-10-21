package com.garced.ingratora.common.event;

import com.garced.ingratora.Ingratora;
import com.garced.ingratora.network.ModMessages;
import com.garced.ingratora.network.OpenInventoryPacket;
import com.garced.ingratora.common.event.ClientModEvents; // Импортируйте ваш класс, где лежит кнопка
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientInputEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Проверяем, что тик завершился и игрок находится непосредственно внутри мира
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            
            // Если игрок нажал нашу кнопку 'C' и у него сейчас не открыто никакое другое меню (чат, сундук и т.д.)
            if (ClientModEvents.INVENTORY_KEY.consumeClick() && Minecraft.getInstance().screen == null) {
                
                // Мгновенно отправляем сетевой пакет на сервер с запросом на открытие
                ModMessages.sendToServer(new OpenInventoryPacket());
            }
        }
    }
}
