package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.entity.trait.MobTrait;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jspecify.annotations.Nullable;

public class MobTraitBuilder extends BuilderBase<MobTrait> {
    public static final ResourceKey<Registry<MobTrait>> REGISTRY = MobTrait.REGISTRY_KEY;

    private boolean champion;
    private boolean taint;
    private boolean replacesAi;
    private final Map<ResourceKey<IAspect>, Integer> aspects = new LinkedHashMap<>();
    private final List<ScriptedMobTrait.AttributeChange> attributeChanges = new ArrayList<>();
    private @Nullable String traitName;
    private @Nullable Consumer<LivingEntity> onAdded;
    private @Nullable Consumer<LivingEntity> onRemoved;
    private @Nullable Consumer<LivingEntity> onTick;
    private ScriptedMobTrait.@Nullable DamageCallback onAttack;
    private ScriptedMobTrait.@Nullable DamageCallback onHurt;

    public MobTraitBuilder(ResourceLocation id) {
        super(id);
    }

    public MobTraitBuilder champion() {
        this.champion = true;
        return this;
    }

    public MobTraitBuilder taint() {
        this.taint = true;
        return this;
    }

    public MobTraitBuilder replacesAi() {
        this.replacesAi = true;
        return this;
    }

    public MobTraitBuilder aspect(ResourceLocation aspect, int amount) {
        aspects.put(ResourceKey.create(IAspect.REGISTRY_KEY, aspect), amount);
        return this;
    }

    public MobTraitBuilder attribute(ResourceLocation attribute, double amount, AttributeModifier.Operation operation) {
        attributeChanges.add(new ScriptedMobTrait.AttributeChange(attribute, amount, operation));
        return this;
    }

    public MobTraitBuilder baseAttribute(ResourceLocation attribute, double value) {
        attributeChanges.add(new ScriptedMobTrait.AttributeChange(attribute, value, null));
        return this;
    }

    public MobTraitBuilder traitName(String name) {
        this.traitName = name;
        return this;
    }

    public MobTraitBuilder onAdded(Consumer<LivingEntity> callback) {
        this.onAdded = callback;
        return this;
    }

    public MobTraitBuilder onRemoved(Consumer<LivingEntity> callback) {
        this.onRemoved = callback;
        return this;
    }

    public MobTraitBuilder onTick(Consumer<LivingEntity> callback) {
        this.onTick = callback;
        return this;
    }

    public MobTraitBuilder onAttack(ScriptedMobTrait.DamageCallback callback) {
        this.onAttack = callback;
        return this;
    }

    public MobTraitBuilder onHurt(ScriptedMobTrait.DamageCallback callback) {
        this.onHurt = callback;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);
        String name = traitName != null
                ? traitName
                : Character.toUpperCase(id.getPath().charAt(0)) + id.getPath().substring(1);
        lang.add(id.getNamespace(), "mob_trait." + id.getNamespace() + "." + id.getPath(), name);
    }

    @Override
    public MobTrait createObject() {
        return new ScriptedMobTrait(
                champion,
                taint,
                replacesAi,
                Map.copyOf(aspects),
                List.copyOf(attributeChanges),
                onAdded,
                onRemoved,
                onTick,
                onAttack,
                onHurt);
    }
}
