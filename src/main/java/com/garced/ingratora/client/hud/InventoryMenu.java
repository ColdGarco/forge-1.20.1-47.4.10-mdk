package com.garced.ingratora.client.hud;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InventoryMenu extends AbstractContainerMenu {
    
    private final Inventory playerInventory;

    // Конструктор для Forge (Сетевой)
    public InventoryMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory);
    }

    // Основной конструктор
    public InventoryMenu(int containerId, Inventory playerInventory) {
        super(com.garced.ingratora.common.init.ModMenuTypes.INVENTORY_MENU.get(), containerId);
        this.playerInventory = playerInventory;

        // Первичная инициализация на сервере
        this.setupRPGLayout(427, 240);
    }

    /**
     * Вызывается экраном для пересчета координат под монитор игрока
     */
    public void updateSlotPositions(int width, int height) {
        this.setupRPGLayout(width, height);
    }

    /**
     * Создание и расстановка всех слотов инвентаря и экипировки
     */
    private void setupRPGLayout(int width, int height) {
        this.slots.clear();

        int centerX = width / 2;
        int centerY = height / 2;

        // =========================================================================
        // 1. ОСНОВНОЙ ИНВЕНТАРЬ СЛЕВА (Сетка 6 рядов по 5 слотов = 18 ячеек, индексы 9-26)
        // =========================================================================
        int invStartX = centerX - 180; 
        int invStartY = centerY - (5 * 22) / 2; 

        int currentSlotIndex = 9; 
        for (int row = 0; row < 6; ++row) {       
            for (int col = 0; col < 5; ++col) {   
                if (currentSlotIndex <= 35) {
                    this.addSlot(new Slot(this.playerInventory, currentSlotIndex, invStartX + col * 22, invStartY + row * 22));
                    currentSlotIndex++;
                }
            }
        }

        // =========================================================================
        // 2. ЭКИПИРОВКА ПОД ПЕРСОНАЖЕМ (Строгий порядок: Шлем -> Торс -> Штаны -> Ноги)
        // =========================================================================
        int equipmentStartX = (centerX + 160);
        int equipmentStartY = centerY - (6 * 22) /2; 

        // Ванильные индексы брони: 39 - Шлем, 38 - Нагрудник, 37 - Поножи, 36 - Ботинки
        int[] armorSlotIndices = new int[]{39, 38, 37, 36};
        net.minecraft.world.entity.EquipmentSlot[] armorTypes = new net.minecraft.world.entity.EquipmentSlot[]{
            net.minecraft.world.entity.EquipmentSlot.HEAD, 
            net.minecraft.world.entity.EquipmentSlot.CHEST, 
            net.minecraft.world.entity.EquipmentSlot.LEGS, 
            net.minecraft.world.entity.EquipmentSlot.FEET
        };

        int armorY = equipmentStartY;
        for (int i = 0; i < 4; ++i) {
            final int exactArmorIndex = armorSlotIndices[i];
            final net.minecraft.world.entity.EquipmentSlot slotType = armorTypes[i];

            this.addSlot(new Slot(this.playerInventory, exactArmorIndex, equipmentStartX, armorY) {
                @Override
                public int getMaxStackSize() { return 1; }
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.canEquip(slotType, InventoryMenu.this.playerInventory.player);
                }
            });
            armorY += 22;
        }

        // Б) Добавляем 2 кастомных RPG слота (Амулет и Кольцо)
        int accessoryY = equipmentStartY + (4 * 22); 
        
        IItemHandler handler = this.playerInventory.player.getCapability(
            com.garced.ingratora.common.player.CharacterAccessoriesProvider.ACCESSORIES_CAP
        ).orElse(null);

        this.addSlot(new RPGAccessorySlot(handler, 0, equipmentStartX, accessoryY));
        this.addSlot(new RPGAccessorySlot(handler, 1, equipmentStartX , accessoryY + 22));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if (index < 27) {
                if (!this.moveItemStackTo(itemstack1, 27, 33, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    /**
     * Независимый RPG-класс слота для аксессуаров
     */
    private static class RPGAccessorySlot extends Slot {
        private final IItemHandler itemHandler;
        private final int index;

        public RPGAccessorySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(new net.minecraft.world.SimpleContainer(1), index, xPosition, yPosition);
            this.itemHandler = itemHandler;
            this.index = index;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (this.itemHandler == null) return false;
            return this.itemHandler.isItemValid(this.index, stack);
        }

        @Override
        public ItemStack getItem() {
            if (this.itemHandler == null) return ItemStack.EMPTY;
            return this.itemHandler.getStackInSlot(this.index);
        }

        @Override
        public void set(ItemStack stack) {
            if (this.itemHandler instanceof net.minecraftforge.items.IItemHandlerModifiable modifiable) {
                modifiable.setStackInSlot(this.index, stack);
            }
            this.setChanged();
        }

        @Override
        public void setByPlayer(ItemStack stack) {
            this.set(stack);
        }

        @Override
        public int getMaxStackSize() { return 1; }

        @Override
        public boolean hasItem() { return !this.getItem().isEmpty(); }

        @Override
        public ItemStack remove(int amount) {
            if (this.itemHandler == null) return ItemStack.EMPTY;
            return this.itemHandler.extractItem(this.index, amount, false);
        }
    }
}