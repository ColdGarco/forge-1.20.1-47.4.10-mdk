package com.garced.ingratora;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireballScrollItem extends Item {
    private static final int MANA_COST = 10;

    public FireballScrollItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide){
            if (!Mana.consume(player, MANA_COST)) {
                player.displayClientMessage(Component.literal("Not enough mana!"), true);
                return InteractionResultHolder.fail(stack);
            }

            Vec3 look = player.getLookAngle();
            ExplodingSmallFireball fireball =
                    new ExplodingSmallFireball(level, player, look.x, look.y, look.z);
            fireball.setPos(
                player.getX()+look.x,
                player.getEyeY()+0.05,
                player.getZ()+look.z
            );
            if (level.addFreshEntity(fireball)) {
                player.getCooldowns().addCooldown(this, 20);
            } else {
                Mana.regenerate(player, MANA_COST);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ingratora.MODID);

    public static final RegistryObject<Item> FIRE_BALL_SCROLL = ITEMS.register("fireball_scroll", 
            () -> new FireballScrollItem(new Item.Properties()));

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(FIRE_BALL_SCROLL.get());
        }
}
}
