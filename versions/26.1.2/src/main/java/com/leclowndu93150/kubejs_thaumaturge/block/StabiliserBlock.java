package com.leclowndu93150.kubejs_thaumaturge.block;

import com.leclowndu93150.kubejs_thaumaturge.builder.StabiliserBlockBuilder;
import com.leclowndu93150.thaumaturge.api.infusion.IInfusionStabiliser;
import com.leclowndu93150.thaumaturge.api.research.IResearchTableAid;
import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class StabiliserBlock extends BasicKubeBlock implements IInfusionStabiliser, IResearchTableAid {
    private final float stabilization;
    private final float researchAid;
    private final @Nullable Identifier countsAs;

    public StabiliserBlock(StabiliserBlockBuilder builder) {
        super(builder);
        this.stabilization = builder.stabilization;
        this.researchAid = builder.researchAid;
        this.countsAs = builder.countsAs;
    }

    @Override
    public boolean canStabiliseInfusion(Level level, BlockPos pos) {
        return stabilization != 0.0F;
    }

    @Override
    public float getStabilizationAmount(Level level, BlockPos pos) {
        return stabilization;
    }

    @Override
    public Block stabiliserIdentity(Level level, BlockPos pos) {
        return countsAs == null ? this : BuiltInRegistries.BLOCK.getValue(countsAs);
    }

    @Override
    public float aspectSaveChance(Level level, BlockPos pos, BlockState state) {
        return researchAid;
    }
}
