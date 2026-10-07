package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.block.CustomJarBlock;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaJar;
import com.leclowndu93150.thaumaturge.registry.TCBlocks;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class JarBlockBuilder extends BlockBuilder {
    public static final ResourceLocation JAR_PARENT_MODEL =
            ResourceLocation.fromNamespaceAndPath("thaumaturge", "block/jar_normal");
    public static final ResourceLocation JAR_ITEM_MODEL =
            ResourceLocation.fromNamespaceAndPath("thaumaturge", "item/bewlr_block");

    public transient int capacity = IEssentiaJar.DEFAULT_CAPACITY;

    public JarBlockBuilder(ResourceLocation id) {
        super(id);
        notSolid();
        parentModel = JAR_PARENT_MODEL;
        textures.put("side", IEssentiaJar.DEFAULT_SIDE_TEXTURE.toString());
        textures.put("top", IEssentiaJar.DEFAULT_TOP_TEXTURE.toString());
        textures.put("bottom", IEssentiaJar.DEFAULT_BOTTOM_TEXTURE.toString());
        textures.put("particle", IEssentiaJar.DEFAULT_SIDE_TEXTURE.toString());
        textures.put("fluid", "thaumaturge:block/animatedglow");
    }

    public JarBlockBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public JarBlockBuilder sideTexture(String texture) {
        textures.put("side", texture);
        textures.put("particle", texture);
        return this;
    }

    public JarBlockBuilder topTexture(String texture) {
        textures.put("top", texture);
        return this;
    }

    public JarBlockBuilder bottomTexture(String texture) {
        textures.put("bottom", texture);
        return this;
    }

    @Override
    protected ItemBuilder getOrCreateItemBuilder() {
        return itemBuilder == null ? (itemBuilder = new JarItemBuilder(id)) : itemBuilder;
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(JAR_ITEM_MODEL);
    }

    @Override
    public Block createObject() {
        if (copyPropertiesFrom == null) {
            copyPropertiesFrom = TCBlocks.JAR_NORMAL.get();
        }
        return new CustomJarBlock(
                createProperties(),
                capacity,
                ResourceLocation.parse(textures.get("side")),
                ResourceLocation.parse(textures.get("top")),
                ResourceLocation.parse(textures.get("bottom")));
    }
}
