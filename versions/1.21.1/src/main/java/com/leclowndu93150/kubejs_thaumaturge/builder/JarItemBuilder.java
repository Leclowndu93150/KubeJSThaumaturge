package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.content.essentia.jar.JarItem;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class JarItemBuilder extends BlockItemBuilder {
    public JarItemBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    public Item createObject() {
        return new JarItem(blockBuilder.get(), createItemProperties());
    }
}
