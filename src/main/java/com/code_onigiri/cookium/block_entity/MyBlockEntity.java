package com.code_onigiri.cookium.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.code_onigiri.cookium.CookiumMod.MY_BLOCK_ENTITY;


public class MyBlockEntity extends BlockEntity {

    public MyBlockEntity(BlockPos pos, BlockState state) {
        super(MY_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MyBlockEntity blockEntity) {
    }
}
