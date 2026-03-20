package com.code_onigiri.cookium.block_entity;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.Nullable;

public class MyBlockMenu extends AbstractContainerMenu {

    private final MyBlockEntity blockEntity;
    private final BlockPos pos;

    public MyBlockMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, BlockPos.ZERO);
    }

    public MyBlockMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        super(CookiumMod.MY_MENU.get(), containerId);
        this.pos = pos;

        if (playerInventory.player.level() != null && playerInventory.player.level().getBlockEntity(pos) instanceof MyBlockEntity be) {
            this.blockEntity = be;
            var handler = be.getItemHandler();
            for (int i = 0; i < 9; i++) {
                this.addSlot(new ResourceHandlerSlot(handler, (IndexModifier<ItemResource>) handler, i, 8 + i * 18, 20));
            }
        } else {
            this.blockEntity = null;
        }

        // プレイヤーインベントリスロット
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        // ホットバー
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack originalStack = stack.copy();

        if (index < 9) {
            if (!this.moveItemStackTo(stack, 9, 45, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) {
            return false;
        }
        return player.distanceToSqr(pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5) <= 64.0;
    }
}
