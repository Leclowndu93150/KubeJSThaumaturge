package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.entity.trait.MobTrait;
import com.leclowndu93150.thaumaturge.api.entity.trait.MobTraits;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public final class MobTraitsWrapper {
    private MobTraitsWrapper() {}

    private static ResourceKey<MobTrait> key(ResourceLocation trait) {
        return ResourceKey.create(MobTrait.REGISTRY_KEY, trait);
    }

    private static Holder<MobTrait> holder(LivingEntity mob, ResourceLocation trait) {
        return mob.registryAccess().lookupOrThrow(MobTrait.REGISTRY_KEY).getOrThrow(key(trait));
    }

    private static @Nullable ResourceLocation id(Holder<MobTrait> trait) {
        return trait.unwrapKey().map(ResourceKey::location).orElse(null);
    }

    public static boolean add(LivingEntity mob, ResourceLocation trait) {
        return MobTraits.add(mob, holder(mob, trait));
    }

    public static boolean remove(LivingEntity mob, ResourceLocation trait) {
        return MobTraits.remove(mob, holder(mob, trait));
    }

    public static boolean has(LivingEntity mob, ResourceLocation trait) {
        return MobTraits.has(mob, key(trait));
    }

    public static List<ResourceLocation> traits(LivingEntity mob) {
        return MobTraits.traits(mob).stream().map(MobTraitsWrapper::id).toList();
    }

    public static @Nullable ResourceLocation champion(LivingEntity mob) {
        return MobTraits.champion(mob).map(MobTraitsWrapper::id).orElse(null);
    }

    public static boolean isChampion(LivingEntity mob) {
        return MobTraits.isChampion(mob);
    }

    public static boolean isTainted(LivingEntity mob) {
        return MobTraits.isTainted(mob);
    }
}
