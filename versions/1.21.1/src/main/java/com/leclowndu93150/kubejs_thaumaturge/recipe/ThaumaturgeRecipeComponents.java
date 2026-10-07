package com.leclowndu93150.kubejs_thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;

public interface ThaumaturgeRecipeComponents {
    RecipeComponentType<AspectList> ASPECT_LIST = RecipeComponentType.unit(
            ResourceLocation.fromNamespaceAndPath("kubejs_thaumaturge", "aspect_list"),
            type -> new SimpleCodecComponent<>(type, AspectList.CODEC, TypeInfo.of(AspectList.class)));

    RecipeComponentType<DataComponentPatch> COMPONENT_PATCH = RecipeComponentType.unit(
            ResourceLocation.fromNamespaceAndPath("kubejs_thaumaturge", "component_patch"),
            type -> new SimpleCodecComponent<>(type, DataComponentPatch.CODEC, TypeInfo.of(DataComponentPatch.class)));

    RecipeComponentType<ResearchGate> RESEARCH_GATE = RecipeComponentType.unit(
            ResourceLocation.fromNamespaceAndPath("kubejs_thaumaturge", "research_gate"),
            type -> new SimpleCodecComponent<>(type, ResearchGate.CODEC, TypeInfo.of(ResearchGate.class)));

    record SimpleCodecComponent<T>(RecipeComponentType<?> type, Codec<T> codec, TypeInfo typeInfo)
            implements RecipeComponent<T> {}
}
