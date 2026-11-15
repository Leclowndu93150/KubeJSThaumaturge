package com.leclowndu93150.kubejs_thaumaturge.block;

import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaJar;
import com.leclowndu93150.thaumaturge.content.essentia.jar.AbstractBlockEntityJar;
import com.leclowndu93150.thaumaturge.content.essentia.jar.AbstractBlockJar;
import com.leclowndu93150.thaumaturge.registry.TTBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class CustomJarBlock extends AbstractBlockJar {
    public static final MapCodec<CustomJarBlock> CODEC = simpleCodec(props -> new CustomJarBlock(
            props,
            IEssentiaJar.DEFAULT_CAPACITY,
            IEssentiaJar.DEFAULT_SIDE_TEXTURE,
            IEssentiaJar.DEFAULT_TOP_TEXTURE,
            IEssentiaJar.DEFAULT_BOTTOM_TEXTURE));

    private final int capacity;
    private final ResourceLocation sideTexture;
    private final ResourceLocation topTexture;
    private final ResourceLocation bottomTexture;

    public CustomJarBlock(
            Properties properties,
            int capacity,
            ResourceLocation sideTexture,
            ResourceLocation topTexture,
            ResourceLocation bottomTexture) {
        super(properties);
        this.capacity = capacity;
        this.sideTexture = sideTexture;
        this.topTexture = topTexture;
        this.bottomTexture = bottomTexture;
    }

    @Override
    protected MapCodec<CustomJarBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<AbstractBlockEntityJar> jarType() {
        return TTBlockEntities.JAR.get();
    }

    @Override
    public int jarCapacity() {
        return capacity;
    }

    @Override
    public ResourceLocation jarSideTexture() {
        return sideTexture;
    }

    @Override
    public ResourceLocation jarTopTexture() {
        return topTexture;
    }

    @Override
    public ResourceLocation jarBottomTexture() {
        return bottomTexture;
    }
}
