package com.code_onigiri.cookium.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import static com.code_onigiri.cookium.CookiumMod.MY_BLOCK_ENTITY;


public class MyBlockEntity extends BlockEntity implements MenuProvider {
    protected final ItemStacksResourceHandler itemHandler = new ItemStacksResourceHandler(9);

    public MyBlockEntity(BlockPos pos, BlockState state) {
        super(MY_BLOCK_ENTITY.get(), pos, state);
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.Item.BLOCK,MY_BLOCK_ENTITY.get(),(myBlockEntity,side) -> myBlockEntity.getItemHandler());
    }

    public ResourceHandler<ItemResource> getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        itemHandler.serialize(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        itemHandler.deserialize(input);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MyBlockEntity blockEntity) {
        if (level == null || level.isClientSide()) return;

        BlockPos EastPos = blockPos.east();
        BlockPos WestPos = blockPos.west();
        BlockEntity EastBE = level.getBlockEntity(EastPos);
        BlockEntity WestBE = level.getBlockEntity(WestPos);

        if (EastBE != null && WestBE != null){
            transfarEastToWest(level, EastBE, WestBE);
        }
    }

    private static void transfarEastToWest(Level level, BlockEntity EastBE, BlockEntity WestBE) {
        var sourceHandler = level.getCapability(Capabilities.Item.BLOCK, EastBE.getBlockPos(), Direction.EAST);
        var targetHandler = level.getCapability(Capabilities.Item.BLOCK, WestBE.getBlockPos(), Direction.WEST);

        if (sourceHandler != null && targetHandler != null){
            try (Transaction tx = Transaction.openRoot()){
                int moved = ResourceHandlerUtil.move(sourceHandler,targetHandler,fr -> !fr.isEmpty(),1,tx);
                if (moved > 0){
                    tx.commit();
                }
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("My Block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MyBlockMenu(i, inventory, this.worldPosition);
    }
}
