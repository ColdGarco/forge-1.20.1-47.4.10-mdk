package com.garced.ingratora.common.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CharacterAccessoriesProvider implements ICapabilitySerializable<CompoundTag> {
    // Токен, по которому наше меню будет находить эти слоты
    public static Capability<IItemHandler> ACCESSORIES_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private CharacterAccessories accessories = null;
    private final LazyOptional<IItemHandler> optional = LazyOptional.of(this::createAccessories);

    private IItemHandler createAccessories() {
        if (this.accessories == null) {
            this.accessories = new CharacterAccessories();
        }
        return this.accessories.getInventory();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ACCESSORIES_CAP) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        createAccessories();
        return this.accessories.saveNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createAccessories();
        this.accessories.loadNBT(nbt);
    }
}
