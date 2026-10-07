package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.block.StabiliserBlock;
import com.leclowndu93150.thaumaturge.api.infusion.IInfusionStabiliser;
import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class StabiliserBlockBuilder extends BasicKubeBlock.Builder {
    public transient float stabilization = IInfusionStabiliser.DEFAULT_STABILIZATION;
    public transient float researchAid;
    public transient @Nullable ResourceLocation countsAs;

    public StabiliserBlockBuilder(ResourceLocation id) {
        super(id);
    }

    public StabiliserBlockBuilder stabilization(float amount) {
        this.stabilization = amount;
        return this;
    }

    public StabiliserBlockBuilder countsAs(ResourceLocation block) {
        this.countsAs = block;
        return this;
    }

    public StabiliserBlockBuilder researchAid(float chance) {
        this.researchAid = chance;
        return this;
    }

    @Override
    public Block createObject() {
        return new StabiliserBlock(this);
    }
}
