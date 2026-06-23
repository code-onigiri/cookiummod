package com.code_onigiri.cookium.block_entity.myblock;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static com.code_onigiri.cookium.CookiumMod.MY_MENU;

public class MyBlockMenu extends AbstractContainerMenu {
    private final Container container;

    public MyBlockMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(9));
    }

    public MyBlockMenu(int containerId, Inventory inventory, Container container) {
        super(MY_MENU.get(), containerId);
        this.container = container;
        container.startOpen(inventory.player);

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(container, i, 8 + i * 18, 18));
        }

        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex < 9) {
                if (!this.moveItemStackTo(stack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 9, false)) {
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
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
