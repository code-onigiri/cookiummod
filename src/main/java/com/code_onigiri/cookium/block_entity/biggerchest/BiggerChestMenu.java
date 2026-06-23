package com.code_onigiri.cookium.block_entity.biggerchest;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import static com.code_onigiri.cookium.CookiumMod.BIGGER_CHEST_MENU;

public class BiggerChestMenu extends AbstractContainerMenu {
    private final Container container;

    public BiggerChestMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(54));
    }

    public BiggerChestMenu(int containerId, Inventory inventory, Container container) {
        super(BIGGER_CHEST_MENU.get(), containerId);
        this.container = container;
        container.startOpen(inventory.player);

        for (int j = 0; j < 6; j++){
            for (int i = 0; i < 9; i++) {
                int slotIndex = i + j * 9;
                this.addSlot(new Slot(container, slotIndex, 8 + i * 18, 18 + j * 18));
            }
        }

        this.addStandardInventorySlots(inventory, 8, 18 + 6 * 18 + 4);
    }

    protected BiggerChestMenu(@Nullable MenuType<?> menuType, int containerId, Container container) {
        super(menuType, containerId);
        this.container = container;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex < 54) {
                if (!this.moveItemStackTo(stack, 54, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 54, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clicked;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
