package com.code_onigiri.cookium.block_entity.myblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import static com.code_onigiri.cookium.CookiumMod.MY_BLOCK_ENTITY;

public class MyBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {
    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    @Nullable private Component customName;

    private final net.neoforged.neoforge.transfer.transaction.SnapshotJournal<ItemStack[]> inventoryJournal =
            new net.neoforged.neoforge.transfer.transaction.SnapshotJournal<>() {
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

    private final ResourceHandler<ItemResource> itemHandler = new ResourceHandler<>() {
        @Override
        public int size() {
            return items.size();
        }

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

    public MyBlockEntity(BlockPos pos, BlockState state) {
        super(MY_BLOCK_ENTITY.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, MY_BLOCK_ENTITY.get(), (be, side) -> be.itemHandler);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(items, slot, count);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        stack.limitSize(stack.getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        for (int slot = 0; slot < items.size(); slot++) {
            items.set(slot, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MyBlockMenu(containerId, inventory, this);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Override
    public @Nullable Component getCustomName() {
        return customName;
    }

    protected Component getDefaultName() {
        return Component.literal("My Block");
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, customName);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.clear();
        ContainerHelper.loadAllItems(input, items);
        customName = parseCustomNameSafe(input, "CustomName");
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        customName = components.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, customName);
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard("CustomName");
        output.discard("Items");
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MyBlockEntity blockEntity) {
        if (level == null || level.isClientSide()) return;

        BlockPos eastPos = blockPos.east();
        BlockPos westPos = blockPos.west();
        BlockEntity eastBE = level.getBlockEntity(eastPos);
        BlockEntity westBE = level.getBlockEntity(westPos);

        if (eastBE != null && westBE != null) {
            transferEastToWest(level, eastBE, westBE);
        }
    }

    private static void transferEastToWest(Level level, BlockEntity eastBE, BlockEntity westBE) {
        BlockState eastState = level.getBlockState(eastBE.getBlockPos());
        BlockState westState = level.getBlockState(westBE.getBlockPos());

        ResourceHandler<ItemResource> source = level.getCapability(
                Capabilities.Item.BLOCK, eastBE.getBlockPos(), eastState, null, Direction.WEST);
        ResourceHandler<ItemResource> target = level.getCapability(
                Capabilities.Item.BLOCK, westBE.getBlockPos(), westState, null, Direction.EAST);
        if (source != null && target != null) {
            try (Transaction tx = Transaction.openRoot()) {
                int moved = ResourceHandlerUtil.move(source, target, fr -> !fr.isEmpty(), 1, tx);
                if (moved > 0) {
                    tx.commit();
                }
            }
        }
    }
}
