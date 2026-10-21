package com.garced.ingratora.common.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

public class CharacterAccessories {
    // Выделяем ровно 2 слота: 0 — Амулет, 1 — Кольцо
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            // Срабатывает автоматически при смене предмета в слоте
        }
    };

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    // Сохранение бижутерии в файл игрока
    public CompoundTag saveNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Inventory", inventory.serializeNBT());
        return nbt;
    }

    // Загрузка бижутерии при заходе на сервер
    public void loadNBT(CompoundTag nbt) {
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }
}
