package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.client.TextureColorizer;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnAssemble;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnUpdate;
import com.leclowndu93150.thaumaturge.api.wands.IWandVisStorage;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.wands.ItemWandRod;
import com.leclowndu93150.thaumaturge.content.wands.WandRodPrimalOnUpdate;
import com.leclowndu93150.thaumaturge.registry.TTWandParts;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public class WandRodItemBuilder extends ItemBuilder {
    private static final Identifier ITEM_COLOR_BASE =
            Identifier.fromNamespaceAndPath("thaumaturge", "item/wand_rod_silverwood");
    private static final Identifier STAFF_COLOR_BASE =
            Identifier.fromNamespaceAndPath("thaumaturge", "item/staff_rod_silverwood");

    public final Identifier partId;
    private int capacity = 25;
    private int craftCost = 1;
    private Identifier rodTexture;
    private boolean customRodTexture;
    private @Nullable Integer color;
    private String wandName;
    private @Nullable ResourceKey<IAspect> chargeAspect;
    private boolean chargeAllPrimals;
    private boolean glow;
    private boolean staff;
    private boolean runes;
    private @Nullable Identifier assemblyGate;
    private @Nullable IWandRodOnAssemble onAssemble;
    private @Nullable IWandRodOnUpdate onUpdate;
    private @Nullable IWandVisStorage visStorage;

    public WandRodItemBuilder(Identifier id) {
        super(id);
        String path =
                id.getPath().startsWith("wand_rod_") ? id.getPath().substring("wand_rod_".length()) : id.getPath();
        this.partId = Identifier.fromNamespaceAndPath(id.getNamespace(), path);
        this.rodTexture =
                Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/models/wand_rod_" + path + ".png");
    }

    public WandRodItemBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public WandRodItemBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandRodItemBuilder rodTexture(Identifier texture) {
        this.rodTexture = texture;
        this.customRodTexture = true;
        return this;
    }

    public WandRodItemBuilder recolor(KubeColor color) {
        this.color = color.kjs$getRGB();
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        super.generateAssets(generator);
        if (color != null) {
            TextureColorizer.generate(
                    generator,
                    id.withPath("item/" + id.getPath()),
                    staff ? STAFF_COLOR_BASE : ITEM_COLOR_BASE,
                    color,
                    false);
            if (!customRodTexture) {
                TextureColorizer.generate(generator, rodTexture, WandRodBuilder.COLOR_BASE, color, false);
            }
        }
    }

    public WandRodItemBuilder primalCharge(Identifier aspect) {
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

    public WandRodItemBuilder assemblyGate(Identifier research) {
        this.assemblyGate = research;
        return this;
    }

    public WandRodItemBuilder onAssemble(IWandRodOnAssemble callback) {
        this.onAssemble = callback;
        return this;
    }

    public WandRodItemBuilder onUpdate(IWandRodOnUpdate callback) {
        this.onUpdate = callback;
        return this;
    }

    public WandRodItemBuilder visStorage(ScriptedWandVisStorage.Reader reader, ScriptedWandVisStorage.Writer writer) {
        this.visStorage = new ScriptedWandVisStorage(reader, writer);
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
        Identifier part = partId;
        return new ItemWandRod(createItemProperties(), () -> TTWandParts.rods().getValue(part));
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        registry.add(WandRod.REGISTRY_KEY, new WandPartEntryBuilder<>(partId, () -> {
            IWandRodOnUpdate charge = null;
            if (chargeAspect != null) {
                charge = new WandRodPrimalOnUpdate(chargeAspect);
            } else if (chargeAllPrimals) {
                charge = new WandRodPrimalOnUpdate();
            }
            return new WandRod(
                    capacity,
                    craftCost,
                    rodTexture,
                    WandRodBuilder.combine(charge, onUpdate),
                    glow,
                    staff,
                    runes,
                    assemblyGate,
                    visStorage,
                    onAssemble);
        }));
    }
}
