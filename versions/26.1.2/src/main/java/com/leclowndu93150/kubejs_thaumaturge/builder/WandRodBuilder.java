package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnAssemble;
import com.leclowndu93150.thaumaturge.api.wands.IWandRodOnUpdate;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import com.leclowndu93150.thaumaturge.content.wands.WandRodPrimalOnUpdate;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public class WandRodBuilder extends BuilderBase<WandRod> {
    public static final ResourceKey<Registry<WandRod>> REGISTRY = WandRod.REGISTRY_KEY;

    private int capacity = 25;
    private int craftCost = 1;
    private Identifier texture;
    private @Nullable ResourceKey<IAspect> chargeAspect;
    private boolean chargeAllPrimals;
    private boolean glow;
    private boolean staff;
    private boolean runes;
    private @Nullable Identifier assemblyGate;
    private @Nullable IWandRodOnAssemble onAssemble;

    public WandRodBuilder(Identifier id) {
        super(id);
        this.texture =
                Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/models/wand_rod_" + id.getPath() + ".png");
    }

    public WandRodBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public WandRodBuilder craftCost(int cost) {
        this.craftCost = cost;
        return this;
    }

    public WandRodBuilder texture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    public WandRodBuilder primalCharge(Identifier aspect) {
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

    public WandRodBuilder assemblyGate(Identifier research) {
        this.assemblyGate = research;
        return this;
    }

    public WandRodBuilder onAssemble(IWandRodOnAssemble callback) {
        this.onAssemble = callback;
        return this;
    }

    public WandRodBuilder runes(boolean runes) {
        this.runes = runes;
        return this;
    }

    @Override
    public WandRod createObject() {
        IWandRodOnUpdate onUpdate = null;
        if (chargeAspect != null) {
            onUpdate = new WandRodPrimalOnUpdate(chargeAspect);
        } else if (chargeAllPrimals) {
            onUpdate = new WandRodPrimalOnUpdate();
        }
        return new WandRod(capacity, craftCost, texture, onUpdate, glow, staff, runes, assemblyGate, null, onAssemble);
    }
}
