package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.client.TextureColorizer;
import com.leclowndu93150.thaumaturge.api.golems.GolemTrait;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemMaterial;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GolemMaterialBuilder extends BuilderBase<GolemMaterial> {
    public static final ResourceKey<Registry<GolemMaterial>> REGISTRY = GolemMaterial.REGISTRY_KEY;
    private static final Identifier COLOR_BASE =
            Identifier.fromNamespaceAndPath("thaumaturge", "textures/block/metal_thaumium.png");
    private static final int WHITE = 0xFFFFFF;

    private final List<Identifier> research = new ArrayList<>();
    private Identifier texture;
    private boolean customTexture;
    private @Nullable Integer color;
    private @Nullable Integer itemColor;
    private int healthMod;
    private int armor;
    private int damage = 1;
    private Identifier base = Identifier.withDefaultNamespace("iron_ingot");
    private Identifier mechanism = Identifier.withDefaultNamespace("iron_ingot");
    private final List<Identifier> traits = new ArrayList<>();
    private boolean antenna;
    private String partName;
    private String partText;

    public GolemMaterialBuilder(Identifier id) {
        super(id);
        this.texture = Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/block/" + id.getPath() + ".png");
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

    public GolemMaterialBuilder research(List<Identifier> entries) {
        this.research.clear();
        this.research.addAll(entries);
        return this;
    }

    public GolemMaterialBuilder texture(Identifier texture) {
        this.texture = texture;
        this.customTexture = true;
        return this;
    }

    public GolemMaterialBuilder recolor(KubeColor color) {
        this.color = color.kjs$getRGB();
        if (!customTexture) {
            this.texture = Identifier.fromNamespaceAndPath(
                    id.getNamespace(), "textures/golem_material/" + id.getPath() + ".png");
        }
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        super.generateAssets(generator);
        if (color != null && !customTexture) {
            TextureColorizer.generate(generator, texture, COLOR_BASE, color, false);
        }
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

    public GolemMaterialBuilder base(Identifier item) {
        this.base = item;
        return this;
    }

    public GolemMaterialBuilder mechanism(Identifier item) {
        this.mechanism = item;
        return this;
    }

    public GolemMaterialBuilder traits(List<Identifier> traits) {
        this.traits.clear();
        this.traits.addAll(traits);
        return this;
    }

    public GolemMaterialBuilder antenna(boolean antenna) {
        this.antenna = antenna;
        return this;
    }

    @Override
    public GolemMaterial createObject() {
        List<Holder<GolemTrait>> resolved = new ArrayList<>(traits.size());
        for (Identifier trait : traits) {
            resolved.add(GolemTraits.holder(trait));
        }
        return new GolemMaterial(
                research,
                texture,
                itemColor != null ? itemColor : color != null ? color : WHITE,
                healthMod,
                armor,
                damage,
                () -> stack(base),
                () -> stack(mechanism),
                resolved,
                antenna);
    }

    private static ItemStack stack(Identifier id) {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(id));
    }
}
