package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.parts.GolemLeg;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GolemLegBuilder extends GolemPartBuilder<GolemLeg> {
    public static final ResourceKey<Registry<GolemLeg>> REGISTRY = GolemLeg.REGISTRY_KEY;

    public GolemLegBuilder(Identifier id) {
        super(id, GolemPartModel.AttachPoint.LEGS);
    }

    @Override
    protected String langKind() {
        return "leg";
    }

    @Override
    public GolemLeg createObject() {
        return new GolemLeg(research, icon, partModel(), components, onTick, resolvedTraits());
    }
}
