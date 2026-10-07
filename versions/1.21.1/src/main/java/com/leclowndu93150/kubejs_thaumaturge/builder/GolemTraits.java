package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.GolemTrait;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class GolemTraits {
    private GolemTraits() {}

    public static Holder<GolemTrait> holder(ResourceLocation id) {
        return DeferredHolder.create(GolemTrait.REGISTRY_KEY, id);
    }
}
