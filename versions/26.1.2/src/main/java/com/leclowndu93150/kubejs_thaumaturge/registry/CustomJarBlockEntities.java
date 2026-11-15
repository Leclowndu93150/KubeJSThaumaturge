package com.leclowndu93150.kubejs_thaumaturge.registry;

import com.leclowndu93150.kubejs_thaumaturge.block.CustomJarBlock;
import com.leclowndu93150.thaumaturge.registry.TTBlockEntities;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.mixins.BlockEntityTypeAccessor;

public final class CustomJarBlockEntities {
    private CustomJarBlockEntities() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(CustomJarBlockEntities::onAddBlocks);
    }

    private static void onAddBlocks(BlockEntityTypeAddBlocksEvent event) {
        BlockEntityType<?> jarType = TTBlockEntities.JAR.get();
        Set<Block> validBlocks = new HashSet<>(jarType.getValidBlocks());
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof CustomJarBlock) {
                validBlocks.add(block);
            }
        }
        ((BlockEntityTypeAccessor) jarType).neoforge$setValidBlocks(validBlocks);
    }
}
