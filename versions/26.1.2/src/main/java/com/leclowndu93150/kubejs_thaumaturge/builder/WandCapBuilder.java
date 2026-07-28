package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class WandCapBuilder extends BuilderBase<WandCap> {
    public static final ResourceKey<Registry<WandCap>> REGISTRY = WandCap.REGISTRY_KEY;

    private float costModifier = 1.0F;
    private final List<ResourceKey<IAspect>> specialAspects = new ArrayList<>();
    private float specialCostModifier;
    private int craftCost = 1;
    private Identifier texture;

    public WandCapBuilder(Identifier id) {
        super(id);
        this.texture =
                Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/models/wand_cap_" + id.getPath() + ".png");
    }

    public WandCapBuilder costModifier(float modifier) {
        this.costModifier = modifier;
        return this;
    }

    public WandCapBuilder specialAspects(List<Identifier> aspects, float modifier) {
        this.specialAspects.clear();
        for (Identifier aspect : aspects) {
            this.specialAspects.add(ResourceKey.create(IAspect.REGISTRY_KEY, aspect));
        }
        this.specialCostModifier = modifier;
        return this;
    }

    public WandCapBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandCapBuilder texture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public WandCap createObject() {
        return new WandCap(costModifier, specialAspects, specialCostModifier, craftCost, texture);
    }
}
