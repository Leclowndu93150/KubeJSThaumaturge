package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.GolemTrait;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemMaterial;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GolemMaterialBuilder extends BuilderBase<GolemMaterial> {
    public static final ResourceKey<Registry<GolemMaterial>> REGISTRY = GolemMaterial.REGISTRY_KEY;

    private final List<ResourceLocation> research = new ArrayList<>();
    private ResourceLocation texture;
    private int itemColor = 0xFFFFFF;
    private int healthMod;
    private int armor;
    private int damage = 1;
    private ResourceLocation base = ResourceLocation.withDefaultNamespace("iron_ingot");
    private ResourceLocation mechanism = ResourceLocation.withDefaultNamespace("iron_ingot");
    private final List<ResourceLocation> traits = new ArrayList<>();
    private String partName;
    private String partText;

    public GolemMaterialBuilder(ResourceLocation id) {
        super(id);
        this.texture = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(), "textures/entity/golems/mat_" + id.getPath() + ".png");
    }

    public GolemMaterialBuilder partName(String name) {
        this.partName = name;
        return this;
    }

    public GolemMaterialBuilder partText(String text) {
        this.partText = text;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);
        lang.add(id.getNamespace(), GolemMaterial.nameKey(id), prettyName());
        lang.add(id.getNamespace(), GolemMaterial.descriptionKey(id), partText != null ? partText : prettyName());
    }

    private String prettyName() {
        String base = partName != null
                ? partName
                : Character.toUpperCase(id.getPath().charAt(0)) + id.getPath().substring(1);
        return base;
    }

    public GolemMaterialBuilder research(List<ResourceLocation> entries) {
        this.research.clear();
        this.research.addAll(entries);
        return this;
    }

    public GolemMaterialBuilder texture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public GolemMaterialBuilder itemColor(int color) {
        this.itemColor = color;
        return this;
    }

    public GolemMaterialBuilder health(int health) {
        this.healthMod = health;
        return this;
    }

    public GolemMaterialBuilder armor(int armor) {
        this.armor = armor;
        return this;
    }

    public GolemMaterialBuilder damage(int damage) {
        this.damage = damage;
        return this;
    }

    public GolemMaterialBuilder base(ResourceLocation item) {
        this.base = item;
        return this;
    }

    public GolemMaterialBuilder mechanism(ResourceLocation item) {
        this.mechanism = item;
        return this;
    }

    public GolemMaterialBuilder traits(List<ResourceLocation> traits) {
        this.traits.clear();
        this.traits.addAll(traits);
        return this;
    }

    @Override
    public GolemMaterial createObject() {
        List<Holder<GolemTrait>> resolved = new ArrayList<>(traits.size());
        for (ResourceLocation trait : traits) {
            resolved.add(GolemTraits.holder(trait));
        }
        return new GolemMaterial(
                research,
                texture,
                itemColor,
                healthMod,
                armor,
                damage,
                () -> stack(base),
                () -> stack(mechanism),
                resolved);
    }

    private static ItemStack stack(ResourceLocation id) {
        return new ItemStack(BuiltInRegistries.ITEM.get(id));
    }
}
