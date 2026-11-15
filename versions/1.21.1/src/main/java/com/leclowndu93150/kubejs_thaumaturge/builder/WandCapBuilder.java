package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.client.TextureColorizer;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

public class WandCapBuilder extends BuilderBase<WandCap> {
    public static final ResourceKey<Registry<WandCap>> REGISTRY = WandCap.REGISTRY_KEY;
    public static final ResourceLocation COLOR_BASE =
            ResourceLocation.fromNamespaceAndPath("thaumaturge", "textures/models/wand_cap_iron.png");

    private float costModifier = 1.0F;
    private final Map<ResourceKey<IAspect>, Float> aspectCosts = new LinkedHashMap<>();
    private int craftCost = 1;
    private ResourceLocation texture;
    private boolean customTexture;
    private @Nullable Integer color;

    public WandCapBuilder(ResourceLocation id) {
        super(id);
        this.texture = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(), "textures/models/wand_cap_" + id.getPath() + ".png");
    }

    public WandCapBuilder costModifier(float modifier) {
        this.costModifier = modifier;
        return this;
    }

    public WandCapBuilder aspectCost(ResourceLocation aspect, float modifier) {
        this.aspectCosts.put(ResourceKey.create(IAspect.REGISTRY_KEY, aspect), modifier);
        return this;
    }

    public WandCapBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandCapBuilder texture(ResourceLocation texture) {
        this.texture = texture;
        this.customTexture = true;
        return this;
    }

    public WandCapBuilder recolor(KubeColor color) {
        this.color = color.kjs$getRGB();
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        super.generateAssets(generator);
        if (color != null && !customTexture) {
            TextureColorizer.generate(generator, texture, COLOR_BASE, color, false);
        }
    }

    @Override
    public WandCap createObject() {
        return new WandCap(costModifier, aspectCosts, craftCost, texture);
    }
}
