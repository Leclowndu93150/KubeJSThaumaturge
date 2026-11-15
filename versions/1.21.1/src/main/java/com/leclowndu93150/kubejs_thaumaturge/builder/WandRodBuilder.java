package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.kubejs_thaumaturge.client.TextureColorizer;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnAssemble;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnUpdate;
import com.leclowndu93150.thaumaturge.api.wands.IWandVisStorage;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.wands.WandRodPrimalOnUpdate;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

public class WandRodBuilder extends BuilderBase<WandRod> {
    public static final ResourceKey<Registry<WandRod>> REGISTRY = WandRod.REGISTRY_KEY;
    public static final ResourceLocation COLOR_BASE =
            ResourceLocation.fromNamespaceAndPath("thaumaturge", "textures/models/wand_rod_silverwood.png");

    private int capacity = 25;
    private int craftCost = 1;
    private ResourceLocation texture;
    private boolean customTexture;
    private @Nullable Integer color;
    private @Nullable ResourceKey<IAspect> chargeAspect;
    private boolean chargeAllPrimals;
    private boolean glow;
    private boolean staff;
    private boolean runes;
    private @Nullable ResourceLocation assemblyGate;
    private @Nullable IWandRodOnAssemble onAssemble;
    private @Nullable IWandRodOnUpdate onUpdate;
    private @Nullable IWandVisStorage visStorage;

    public WandRodBuilder(ResourceLocation id) {
        super(id);
        this.texture = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(), "textures/models/wand_rod_" + id.getPath() + ".png");
    }

    public WandRodBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public WandRodBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandRodBuilder texture(ResourceLocation texture) {
        this.texture = texture;
        this.customTexture = true;
        return this;
    }

    public WandRodBuilder recolor(KubeColor color) {
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

    public WandRodBuilder primalCharge(ResourceLocation aspect) {
        this.chargeAspect = ResourceKey.create(IAspect.REGISTRY_KEY, aspect);
        this.chargeAllPrimals = false;
        return this;
    }

    public WandRodBuilder primalCharge() {
        this.chargeAspect = null;
        this.chargeAllPrimals = true;
        return this;
    }

    public WandRodBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public WandRodBuilder staff(boolean staff) {
        this.staff = staff;
        return this;
    }

    public WandRodBuilder assemblyGate(ResourceLocation research) {
        this.assemblyGate = research;
        return this;
    }

    public WandRodBuilder onAssemble(IWandRodOnAssemble callback) {
        this.onAssemble = callback;
        return this;
    }

    public WandRodBuilder onUpdate(IWandRodOnUpdate callback) {
        this.onUpdate = callback;
        return this;
    }

    public WandRodBuilder visStorage(ScriptedWandVisStorage.Reader reader, ScriptedWandVisStorage.Writer writer) {
        this.visStorage = new ScriptedWandVisStorage(reader, writer);
        return this;
    }

    public WandRodBuilder runes(boolean runes) {
        this.runes = runes;
        return this;
    }

    @Override
    public WandRod createObject() {
        IWandRodOnUpdate charge = null;
        if (chargeAspect != null) {
            charge = new WandRodPrimalOnUpdate(chargeAspect);
        } else if (chargeAllPrimals) {
            charge = new WandRodPrimalOnUpdate();
        }
        return new WandRod(
                capacity,
                craftCost,
                texture,
                combine(charge, onUpdate),
                glow,
                staff,
                runes,
                assemblyGate,
                visStorage,
                onAssemble);
    }

    public static @Nullable IWandRodOnUpdate combine(
            @Nullable IWandRodOnUpdate first, @Nullable IWandRodOnUpdate second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        return (wand, player) -> {
            first.onUpdate(wand, player);
            second.onUpdate(wand, player);
        };
    }
}
