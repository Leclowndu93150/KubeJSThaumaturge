package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnAssemble;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnUpdate;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandRod;
import com.leclowndu93150.thaumaturge.content.wands.WandRodPrimalOnUpdate;
import com.leclowndu93150.thaumaturge.registry.TCWandParts;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public class WandRodItemBuilder extends ItemBuilder {
    public final ResourceLocation partId;
    private int capacity = 25;
    private int craftCost = 1;
    private ResourceLocation rodTexture;
    private String wandName;
    private @Nullable ResourceKey<IAspect> chargeAspect;
    private boolean chargeAllPrimals;
    private boolean glow;
    private boolean staff;
    private boolean runes;
    private @Nullable ResourceLocation assemblyGate;
    private @Nullable IWandRodOnAssemble onAssemble;

    public WandRodItemBuilder(ResourceLocation id) {
        super(id);
        String path =
                id.getPath().startsWith("wand_rod_") ? id.getPath().substring("wand_rod_".length()) : id.getPath();
        this.partId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path);
        this.rodTexture =
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/models/wand_rod_" + path + ".png");
    }

    public WandRodItemBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public WandRodItemBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandRodItemBuilder rodTexture(ResourceLocation texture) {
        this.rodTexture = texture;
        return this;
    }

    public WandRodItemBuilder primalCharge(ResourceLocation aspect) {
        this.chargeAspect = ResourceKey.create(IAspect.REGISTRY_KEY, aspect);
        this.chargeAllPrimals = false;
        return this;
    }

    public WandRodItemBuilder primalCharge() {
        this.chargeAspect = null;
        this.chargeAllPrimals = true;
        return this;
    }

    public WandRodItemBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public WandRodItemBuilder staff(boolean staff) {
        this.staff = staff;
        return this;
    }

    public WandRodItemBuilder assemblyGate(ResourceLocation research) {
        this.assemblyGate = research;
        return this;
    }

    public WandRodItemBuilder onAssemble(IWandRodOnAssemble callback) {
        this.onAssemble = callback;
        return this;
    }

    public WandRodItemBuilder runes(boolean runes) {
        this.runes = runes;
        return this;
    }

    public WandRodItemBuilder wandName(String name) {
        this.wandName = name;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);
        String name = wandName != null ? wandName : defaultWandName();
        lang.add(partId.getNamespace(), "wand.thaumaturge.rod." + partId.getPath(), name);
    }

    private String defaultWandName() {
        String pretty = Character.toUpperCase(partId.getPath().charAt(0))
                + partId.getPath().substring(1);
        return pretty;
    }

    @Override
    public Item createObject() {
        ResourceLocation part = partId;
        return new ItemWandRod(createItemProperties(), () -> TCWandParts.rods().get(part));
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        registry.add(WandRod.REGISTRY_KEY, new WandPartEntryBuilder<>(partId, () -> {
            IWandRodOnUpdate onUpdate = null;
            if (chargeAspect != null) {
                onUpdate = new WandRodPrimalOnUpdate(chargeAspect);
            } else if (chargeAllPrimals) {
                onUpdate = new WandRodPrimalOnUpdate();
            }
            return new WandRod(
                    capacity, craftCost, rodTexture, onUpdate, glow, staff, runes, assemblyGate, null, onAssemble);
        }));
    }
}
