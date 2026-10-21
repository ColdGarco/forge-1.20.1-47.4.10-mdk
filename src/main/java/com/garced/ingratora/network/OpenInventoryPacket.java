package com.garced.ingratora.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class OpenInventoryPacket {
    
    // Конструктор должен быть пустым
    public OpenInventoryPacket() {}

    // Forge требует, чтобы мы записали что-то в буфер, даже если пакет пустой
    public static void encode(OpenInventoryPacket msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(true); // Пишем заглушку
    }

    // Читаем заглушку обратно
    public static OpenInventoryPacket decode(FriendlyByteBuf buffer) {
        buffer.readBoolean(); 
        return new OpenInventoryPacket();
    }

    public static void handle(OpenInventoryPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();
        context.enqueueWork(() -> {
            // Этот код выполняется СТРОГО НА СЕРВЕРЕ
            ServerPlayer player = context.getSender();
            if (player != null) {
                System.out.println("Сервер получил пакет от игрока: " + player.getName().getString());
                
                // Открываем серверный контейнер меню
                player.openMenu(new SimpleMenuProvider(
                (containerId, playerInventory, p) -> new com.garced.ingratora.client.hud.InventoryMenu(containerId, playerInventory),
                Component.literal("Inventory")
)); // Пустой буфер для доп. данных
            }
        });
        context.setPacketHandled(true);
    }
}