package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.parts.GolemHead;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GolemHeadBuilder extends GolemPartBuilder<GolemHead> {
    public static final ResourceKey<Registry<GolemHead>> REGISTRY = GolemHead.REGISTRY_KEY;

    public GolemHeadBuilder(Identifier id) {
        super(id, GolemPartModel.AttachPoint.HEAD);
    }

    @Override
    protected String langKind() {
        return "head";
    }

    @Override
    public GolemHead createObject() {
        return new GolemHead(research, icon, partModel(), components, onTick, resolvedTraits());
    }
}
