package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaJar;
import com.leclowndu93150.thaumaturge.content.essentia.jar.BlockJarCustom;
import com.leclowndu93150.thaumaturge.registry.TCBlocks;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class JarBlockBuilder extends BlockBuilder {
    public static final Identifier JAR_PARENT_MODEL =
            Identifier.fromNamespaceAndPath("thaumaturge", "block/jar_normal");

    public transient int capacity = IEssentiaJar.DEFAULT_CAPACITY;

    public JarBlockBuilder(Identifier id) {
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
        return itemBuilder == null ? (itemBuilder = new JarItemBuilder(this, id)) : itemBuilder;
    }

    @Override
    public Block createObject() {
        if (copyPropertiesFrom == null) {
            copyPropertiesFrom = TCBlocks.JAR_NORMAL.get();
        }
        return new BlockJarCustom(
                createProperties(),
                capacity,
                Identifier.parse(textures.get("side")),
                Identifier.parse(textures.get("top")),
                Identifier.parse(textures.get("bottom")));
    }
}
