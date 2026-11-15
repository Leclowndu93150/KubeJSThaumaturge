package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.block.CustomJarBlock;
import com.leclowndu93150.kubejs_thaumaturge.client.TextureColorizer;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaJar;
import com.leclowndu93150.thaumaturge.registry.TTBlocks;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class JarBlockBuilder extends BlockBuilder {
    public static final Identifier JAR_PARENT_MODEL =
            Identifier.fromNamespaceAndPath("thaumaturge", "block/jar_normal");

    public transient int capacity = IEssentiaJar.DEFAULT_CAPACITY;
    private transient @Nullable Integer color;

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

    public JarBlockBuilder recolor(KubeColor color) {
        this.color = color.kjs$getRGB();
        sideTexture(colorSprite("side").toString());
        topTexture(colorSprite("top").toString());
        return this;
    }

    private Identifier colorSprite(String face) {
        return id.withPath("block/" + id.getPath() + "_" + face);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        if (color != null) {
            generateColored(generator, "side", IEssentiaJar.DEFAULT_SIDE_TEXTURE);
            generateColored(generator, "top", IEssentiaJar.DEFAULT_TOP_TEXTURE);
        }
        super.generateAssets(generator);
    }

    private void generateColored(KubeAssetGenerator generator, String face, Identifier base) {
        Identifier sprite = colorSprite(face);
        if (sprite.toString().equals(textures.get(face))) {
            TextureColorizer.generate(generator, sprite, base, color, true);
        }
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
            copyPropertiesFrom = TTBlocks.JAR_NORMAL.get();
        }
        return new CustomJarBlock(
                createProperties(),
                capacity,
                Identifier.parse(textures.get("side")),
                Identifier.parse(textures.get("top")),
                Identifier.parse(textures.get("bottom")));
    }
}
