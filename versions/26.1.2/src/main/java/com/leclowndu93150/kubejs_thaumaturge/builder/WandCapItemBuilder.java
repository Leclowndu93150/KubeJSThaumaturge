package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.client.TextureColorizer;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandCap;
import com.leclowndu93150.thaumaturge.registry.TTWandParts;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public class WandCapItemBuilder extends ItemBuilder {
    private static final Identifier ITEM_COLOR_BASE =
            Identifier.fromNamespaceAndPath("thaumaturge", "item/wand_cap_iron");

    public final Identifier partId;
    private float costModifier = 1.0F;
    private final Map<ResourceKey<IAspect>, Float> aspectCosts = new LinkedHashMap<>();
    private int craftCost = 1;
    private Identifier capTexture;
    private boolean customCapTexture;
    private @Nullable Integer color;
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

    public WandCapItemBuilder aspectCost(Identifier aspect, float modifier) {
        this.aspectCosts.put(ResourceKey.create(IAspect.REGISTRY_KEY, aspect), modifier);
        return this;
    }

    public WandCapItemBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandCapItemBuilder capTexture(Identifier texture) {
        this.capTexture = texture;
        this.customCapTexture = true;
        return this;
    }

    public WandCapItemBuilder recolor(KubeColor color) {
        this.color = color.kjs$getRGB();
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        super.generateAssets(generator);
        if (color != null) {
            TextureColorizer.generate(generator, id.withPath("item/" + id.getPath()), ITEM_COLOR_BASE, color, false);
            if (!customCapTexture) {
                TextureColorizer.generate(generator, capTexture, WandCapBuilder.COLOR_BASE, color, false);
            }
        }
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
        return new ItemWandCap(createItemProperties(), () -> TTWandParts.caps().getValue(part));
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        registry.add(
                WandCap.REGISTRY_KEY,
                new WandPartEntryBuilder<>(
                        partId, () -> new WandCap(costModifier, aspectCosts, craftCost, capTexture)));
    }
}
