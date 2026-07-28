package com.leclowndu93150.kubejs_thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;

public interface ThaumaturgeRecipeComponents {
    ResourceKey<RecipeComponentType<?>> ASPECT_LIST_KEY =
            RecipeComponentType.key(Identifier.fromNamespaceAndPath("kubejs_thaumaturge", "aspect_list"));
    ResourceKey<RecipeComponentType<?>> ITEM_STACK_TEMPLATE_KEY =
            RecipeComponentType.key(Identifier.fromNamespaceAndPath("kubejs_thaumaturge", "item_stack_template"));
    ResourceKey<RecipeComponentType<?>> COMPONENT_PATCH_KEY =
            RecipeComponentType.key(Identifier.fromNamespaceAndPath("kubejs_thaumaturge", "component_patch"));
    ResourceKey<RecipeComponentType<?>> RESEARCH_GATE_KEY =
            RecipeComponentType.key(Identifier.fromNamespaceAndPath("kubejs_thaumaturge", "research_gate"));

    RecipeComponent<AspectList> ASPECT_LIST =
            new SimpleCodecComponent<>(ASPECT_LIST_KEY, AspectList.CODEC, TypeInfo.of(AspectList.class));

    RecipeComponent<ItemStackTemplate> ITEM_STACK_TEMPLATE = new SimpleCodecComponent<>(
            ITEM_STACK_TEMPLATE_KEY, ItemStackTemplate.CODEC, TypeInfo.of(ItemStackTemplate.class));

    RecipeComponent<DataComponentPatch> COMPONENT_PATCH = new SimpleCodecComponent<>(
            COMPONENT_PATCH_KEY, DataComponentPatch.CODEC, TypeInfo.of(DataComponentPatch.class));

    RecipeComponent<ResearchGate> RESEARCH_GATE =
            new SimpleCodecComponent<>(RESEARCH_GATE_KEY, ResearchGate.CODEC, TypeInfo.of(ResearchGate.class));

    record SimpleCodecComponent<T>(ResourceKey<RecipeComponentType<?>> type, Codec<T> codec, TypeInfo typeInfo)
            implements RecipeComponent<T> {}
}
