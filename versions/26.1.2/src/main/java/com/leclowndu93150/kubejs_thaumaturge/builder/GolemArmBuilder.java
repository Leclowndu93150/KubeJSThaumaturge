package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.parts.GolemArm;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class GolemArmBuilder extends GolemPartBuilder<GolemArm> {
    public static final ResourceKey<Registry<GolemArm>> REGISTRY = GolemArm.REGISTRY_KEY;

    public GolemArmBuilder(Identifier id) {
        super(id, GolemPartModel.AttachPoint.ARMS);
    }

    @Override
    protected String langKind() {
        return "arm";
    }

    @Override
    public GolemArm createObject() {
        return new GolemArm(research, icon, partModel(), components, null, resolvedTraits());
    }
}
