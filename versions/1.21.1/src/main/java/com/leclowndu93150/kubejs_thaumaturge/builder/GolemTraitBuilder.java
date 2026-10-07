package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.GolemTrait;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

public class GolemTraitBuilder extends BuilderBase<GolemTrait> {
    public static final ResourceKey<Registry<GolemTrait>> REGISTRY = GolemTrait.REGISTRY_KEY;

    private @Nullable ResourceLocation icon;
    private @Nullable ResourceKey<GolemTrait> opposite;
    private String partName;
    private String partText;

    public GolemTraitBuilder(ResourceLocation id) {
        super(id);
    }

    public GolemTraitBuilder icon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public GolemTraitBuilder opposite(ResourceLocation trait) {
        this.opposite = ResourceKey.create(GolemTrait.REGISTRY_KEY, trait);
        return this;
    }

    public GolemTraitBuilder partName(String name) {
        this.partName = name;
        return this;
    }

    public GolemTraitBuilder partText(String text) {
        this.partText = text;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);
        String base = partName != null
                ? partName
                : Character.toUpperCase(id.getPath().charAt(0)) + id.getPath().substring(1);
        lang.add(id.getNamespace(), GolemTrait.nameKey(id), base);
        lang.add(id.getNamespace(), GolemTrait.descriptionKey(id), partText != null ? partText : base);
    }

    @Override
    public GolemTrait createObject() {
        return icon == null ? GolemTrait.ofId(id, opposite) : new GolemTrait(icon, opposite);
    }
}
