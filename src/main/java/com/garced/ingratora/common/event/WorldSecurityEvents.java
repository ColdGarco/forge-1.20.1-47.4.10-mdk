package com.garced.ingratora.common.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.Event;
import com.garced.ingratora.Ingratora;


@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class WorldSecurityEvents {  
    // Глобальное правило: Отмена разрушения блоков игроками, если они не в креативе
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity(); // В новых версиях используется getEntity() вместо getPlayer()
        
        if (player != null && !player.isCreative()) {
            // Отменяем событие полностью
            event.setCanceled(true); 
        }
    }

    // Глобальное правило: Отмена установки блоков игроками, если они не в креативе
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // Проверяем, что блок пытается поставить именно игрок
        if (event.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                event.setCanceled(true); // Отменяем установку блока
            }
        }
    }
    // Глобальное правило: Отмена спавна ванильных мобов
    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        
        // Получаем ID моба (например, "minecraft:creeper")
        String entityId = EntityType.getKey(mob.getType()).toString();
        
        // Если это ванильный моб
        if (entityId.startsWith("minecraft:")) {
            
            // Полностью блокируем спавн на самом раннем этапе
            event.setSpawnCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }
}
