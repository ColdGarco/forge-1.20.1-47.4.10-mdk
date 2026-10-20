package com.garced.ingratora.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class ModBlock extends Block {
    protected ModBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
