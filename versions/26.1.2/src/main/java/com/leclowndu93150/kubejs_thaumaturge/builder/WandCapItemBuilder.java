package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandCap;
import com.leclowndu93150.thaumaturge.registry.TCWandParts;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class WandCapItemBuilder extends ItemBuilder {
    public final Identifier partId;
    private float costModifier = 1.0F;
    private final List<ResourceKey<IAspect>> specialAspects = new ArrayList<>();
    private float specialCostModifier;
    private int craftCost = 1;
    private Identifier capTexture;
    private String wandName;

    public WandCapItemBuilder(Identifier id) {
        super(id);
        String path =
                id.getPath().startsWith("wand_cap_") ? id.getPath().substring("wand_cap_".length()) : id.getPath();
        this.partId = Identifier.fromNamespaceAndPath(id.getNamespace(), path);
        this.capTexture =
                Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/models/wand_cap_" + path + ".png");
    }

    public WandCapItemBuilder costModifier(float modifier) {
        this.costModifier = modifier;
        return this;
    }

    public WandCapItemBuilder specialAspects(List<Identifier> aspects, float modifier) {
        this.specialAspects.clear();
        for (Identifier aspect : aspects) {
            this.specialAspects.add(ResourceKey.create(IAspect.REGISTRY_KEY, aspect));
        }
        this.specialCostModifier = modifier;
        return this;
    }

    public WandCapItemBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandCapItemBuilder capTexture(Identifier texture) {
        this.capTexture = texture;
        return this;
    }

    public WandCapItemBuilder wandName(String name) {
        this.wandName = name;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);
        String name = wandName != null ? wandName : defaultWandName();
        lang.add(partId.getNamespace(), "wand.thaumaturge.cap." + partId.getPath(), name);
    }

    private String defaultWandName() {
        String pretty = Character.toUpperCase(partId.getPath().charAt(0))
                + partId.getPath().substring(1);
        return pretty + " Capped";
    }

    @Override
    public Item createObject() {
        Identifier part = partId;
        return new ItemWandCap(createItemProperties(), () -> TCWandParts.caps().getValue(part));
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        registry.add(
                WandCap.REGISTRY_KEY,
                new WandPartEntryBuilder<>(
                        partId,
                        () -> new WandCap(costModifier, specialAspects, specialCostModifier, craftCost, capTexture)));
    }
}
