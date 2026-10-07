package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.parts.GolemAddon;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class GolemAddonBuilder extends GolemPartBuilder<GolemAddon> {
    public static final ResourceKey<Registry<GolemAddon>> REGISTRY = GolemAddon.REGISTRY_KEY;

    public GolemAddonBuilder(ResourceLocation id) {
        super(id, GolemPartModel.AttachPoint.BODY);
    }

    @Override
    protected String langKind() {
        return "addon";
    }

    @Override
    public GolemAddon createObject() {
        return new GolemAddon(research, icon, partModel(), components, null, resolvedTraits());
    }
}
