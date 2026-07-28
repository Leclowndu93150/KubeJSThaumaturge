package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.content.essentia.jar.JarItem;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.util.ID;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class JarItemBuilder extends BlockItemBuilder {
    public JarItemBuilder(BlockBuilder b, Identifier i) {
        super(b, i);
    }

    @Override
    public Item createObject() {
        return new JarItem(
                blockBuilder.get(),
                createItemProperties().overrideDescription(blockBuilder.getBuilderTranslationKey()));
    }

    @Override
    public boolean hasCustomModel() {
        return true;
    }

    @Override
    public void generateItemModels(KubeAssetGenerator generator) {
        String blockModel = Identifier.fromNamespaceAndPath(
                        blockBuilder.id.getNamespace(), "block/" + blockBuilder.id.getPath())
                .toString();

        JsonObject modelPart = new JsonObject();
        modelPart.addProperty("type", "minecraft:model");
        modelPart.addProperty("model", blockModel);

        JsonObject specialModel = new JsonObject();
        specialModel.addProperty("type", "thaumaturge:jar");

        JsonObject specialPart = new JsonObject();
        specialPart.addProperty("type", "minecraft:special");
        specialPart.addProperty("base", blockModel);
        specialPart.add("model", specialModel);

        JsonArray models = new JsonArray();
        models.add(modelPart);
        models.add(specialPart);

        JsonObject composite = new JsonObject();
        composite.addProperty("type", "minecraft:composite");
        composite.add("models", models);

        JsonObject definition = new JsonObject();
        definition.add("model", composite);

        generator.json(id.withPath(ID.ITEM_DEFINITION), definition);
    }
}
