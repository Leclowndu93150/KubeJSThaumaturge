package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.entity.trait.MobTrait;
import com.leclowndu93150.thaumaturge.api.entity.trait.MobTraitModifiers;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jspecify.annotations.Nullable;

public record ScriptedMobTrait(
        boolean champion,
        boolean taint,
        boolean replacesAi,
        Map<ResourceKey<IAspect>, Integer> traitAspects,
        List<AttributeChange> attributeChanges,
        @Nullable Consumer<LivingEntity> added,
        @Nullable Consumer<LivingEntity> removed,
        @Nullable Consumer<LivingEntity> ticked,
        @Nullable DamageCallback attack,
        @Nullable DamageCallback hurt)
        implements MobTrait {
    @FunctionalInterface
    public interface DamageCallback {
        @Nullable
        Object apply(LivingEntity mob, @Nullable LivingEntity other, DamageSource source, float amount);
    }

    public record AttributeChange(
            Identifier attribute, double amount, AttributeModifier.@Nullable Operation operation) {
        private void apply(MobTraitModifiers modifiers) {
            Holder<Attribute> holder = holder(attribute);
            if (operation == null) {
                modifiers.setBase(holder, amount);
            } else {
                modifiers.add(holder, amount, operation);
            }
        }

        private static Holder<Attribute> holder(Identifier id) {
            return BuiltInRegistries.ATTRIBUTE.get(id).orElseThrow();
        }
    }

    private static float damage(
            @Nullable DamageCallback callback,
            LivingEntity mob,
            @Nullable LivingEntity other,
            DamageSource source,
            float amount) {
        if (callback == null) {
            return amount;
        }
        return callback.apply(mob, other, source, amount) instanceof Number changed
                        && !Float.isNaN(changed.floatValue())
                ? changed.floatValue()
                : amount;
    }

    @Override
    public boolean isChampion() {
        return champion;
    }

    @Override
    public boolean isTaint() {
        return taint;
    }

    @Override
    public boolean replacesNativeAi(LivingEntity mob) {
        return replacesAi;
    }

    @Override
    public void modifiers(LivingEntity mob, MobTraitModifiers modifiers) {
        for (AttributeChange change : attributeChanges) {
            change.apply(modifiers);
        }
    }

    @Override
    public AspectList aspects(LivingEntity mob) {
        HolderLookup.RegistryLookup<IAspect> lookup = mob.registryAccess().lookupOrThrow(IAspect.REGISTRY_KEY);
        AspectList aspects = AspectList.EMPTY;
        for (Map.Entry<ResourceKey<IAspect>, Integer> entry : traitAspects.entrySet()) {
            aspects = aspects.add(lookup.getOrThrow(entry.getKey()), entry.getValue());
        }
        return aspects;
    }

    @Override
    public void onAdded(LivingEntity mob) {
        if (added != null) {
            added.accept(mob);
        }
    }

    @Override
    public void onRemoved(LivingEntity mob) {
        if (removed != null) {
            removed.accept(mob);
        }
    }

    @Override
    public void tick(LivingEntity mob) {
        if (ticked != null) {
            ticked.accept(mob);
        }
    }

    @Override
    public float onAttack(LivingEntity mob, LivingEntity target, DamageSource source, float amount) {
        return damage(attack, mob, target, source, amount);
    }

    @Override
    public float onHurt(LivingEntity mob, @Nullable LivingEntity attacker, DamageSource source, float amount) {
        return damage(hurt, mob, attacker, source, amount);
    }
}
