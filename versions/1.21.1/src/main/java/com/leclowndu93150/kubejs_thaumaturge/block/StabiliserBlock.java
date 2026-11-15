package com.leclowndu93150.kubejs_thaumaturge.block;

import com.leclowndu93150.kubejs_thaumaturge.builder.StabiliserBlockBuilder;
import com.leclowndu93150.thaumaturge.api.infusion.AltarStabiliser;
import com.leclowndu93150.thaumaturge.api.research.IResearchTableAid;
import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class StabiliserBlock extends BasicKubeBlock implements AltarStabiliser, IResearchTableAid {
    private final float stabilization;
    private final float researchAid;
    private final @Nullable ResourceLocation countsAs;

    public StabiliserBlock(StabiliserBlockBuilder builder) {
        super(builder);
        this.stabilization = builder.stabilization;
        this.researchAid = builder.researchAid;
        this.countsAs = builder.countsAs;
    }

    @Override
    public boolean stabilisesNow(Level level, BlockPos pos) {
        return stabilization != 0.0F;
    }

    @Override
    public float pairValue(Level level, BlockPos pos) {
        return stabilization;
    }

    @Override
    public Block countsAs(Level level, BlockPos pos) {
        return countsAs == null ? this : BuiltInRegistries.BLOCK.get(countsAs);
    }

    @Override
    public float aspectSaveChance(Level level, BlockPos pos, BlockState state) {
        return researchAid;
    }
}
