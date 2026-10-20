package com.garced.ingratora.common.spell;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
import com.garced.ingratora.Ingratora;
import com.garced.ingratora.global.Mana;

@Mod.EventBusSubscriber(modid = Ingratora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LightBeamScrollItem extends Item {
    private static final int MANA_COST = 10;

    public LightBeamScrollItem(Properties properties) {
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
        }
    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ingratora.MODID);

    public static final RegistryObject<Item> LIGHT_BEAM_SCROLL = ITEMS.register("lightbeam_scroll", 
            () -> new LightBeamScrollItem(new Item.Properties()));
    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(LIGHT_BEAM_SCROLL.get());
        }
    }
}
