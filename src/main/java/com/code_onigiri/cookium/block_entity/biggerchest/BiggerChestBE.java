package com.code_onigiri.cookium.block_entity.biggerchest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.NonNull;

import static com.code_onigiri.cookium.CookiumMod.BIGGER_CHEST_BE;

public class BiggerChestBE extends BaseContainerBlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(54, ItemStack.EMPTY);

    private final SnapshotJournal<ItemStack[]> inventoryJournal =
            new SnapshotJournal<>() {
                @Override
                protected ItemStack[] createSnapshot() {
                    ItemStack[] snapshot = new ItemStack[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        snapshot[i] = items.get(i).copy();
                    }
                    return snapshot;
                }

                @Override
                protected void revertToSnapshot(ItemStack[] snapshot) {
                    for (int i = 0; i < snapshot.length; i++) {
                        items.set(i, snapshot[i].copy());
                    }
                }

                @Override
                protected void onRootCommit(ItemStack[] originalState) {
                    if (!isRemoved()) {
                        setChanged();
                    }
                }
            };

    private final ResourceHandler<ItemResource> itemHandler = new ItemStacksResourceHandler(this.items) {
//        @Override
//        public int size() {
//            return items.size();
//        }

        @Override
        public ItemResource getResource(int slot) {
            return ItemResource.of(items.get(slot));
        }

        @Override
        public long getAmountAsLong(int slot) {
            return items.get(slot).getCount();
        }

        @Override
        public long getCapacityAsLong(int slot, ItemResource resource) {
            return items.get(slot).getMaxStackSize();
        }

        @Override
        public boolean isValid(int slot, ItemResource resource) {
            ItemStack slotStack = items.get(slot);
            return slotStack.isEmpty() || ItemStack.isSameItemSameComponents(slotStack, resource.toStack(1));
        }

        @Override
        public int insert(int slot, ItemResource resource, int amount, TransactionContext tx) {
            if (amount <= 0) {
                return 0;
            }

            ItemStack current = items.get(slot);
            ItemStack toInsert = resource.toStack(amount);

            if (!current.isEmpty() && !ItemStack.isSameItemSameComponents(current, toInsert)) {
                return 0;
            }
            int canAdd = current.isEmpty() ? toInsert.getMaxStackSize() : current.getMaxStackSize() - current.getCount();
            int actuallyAdded = Math.min(amount, canAdd);

            if (actuallyAdded > 0) {
                inventoryJournal.updateSnapshots(tx);
                if (current.isEmpty()) {
                    items.set(slot, toInsert.copyWithCount(actuallyAdded));
                } else {
                    current.grow(actuallyAdded);
                }
            }
            return actuallyAdded;
        }

        @Override
        public int extract(int slot, ItemResource resource, int amount, TransactionContext tx) {
            if (amount <= 0) {
                return 0;
            }

            ItemStack current = items.get(slot);
            if (current.isEmpty()) return 0;

            ItemStack template = resource.toStack(1);
            if (!ItemStack.isSameItemSameComponents(current, template)) return 0;

            int toExtract = Math.min(amount, current.getCount());

            if (toExtract > 0) {
                inventoryJournal.updateSnapshots(tx);
                current.shrink(toExtract);
                if (current.isEmpty()) {
                    items.set(slot, ItemStack.EMPTY);
                }
            }
            return toExtract;
        }
    };

    public BiggerChestBE( BlockPos worldPosition, BlockState blockState) {
        super(BIGGER_CHEST_BE.get(), worldPosition, blockState);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, BIGGER_CHEST_BE.get(), (be, side) -> be.itemHandler);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.literal("Bigger Chest");
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.items.clear();
        int slotCount = Math.min(this.items.size(), items.size());
        for (int slot = 0; slot < slotCount; slot++) {
            this.items.set(slot, items.get(slot));
        }
    }

    @Override
    protected @NonNull AbstractContainerMenu createMenu(int i, @NonNull Inventory inventory) {
        return new BiggerChestMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }
}
