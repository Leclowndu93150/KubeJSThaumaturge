package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.research.pool.AspectPoolAccess;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class AspectPoolsWrapper {
    private AspectPoolsWrapper() {}

    private static Holder<IAspect> holder(Player player, ResourceLocation aspect) {
        return player.level()
                .registryAccess()
                .lookupOrThrow(IAspect.REGISTRY_KEY)
                .getOrThrow(ResourceKey.create(IAspect.REGISTRY_KEY, aspect));
    }

    public static boolean isDiscovered(Player player, ResourceLocation aspect) {
        return AspectPoolAccess.isDiscovered(player, holder(player, aspect));
    }

    public static int amount(Player player, ResourceLocation aspect) {
        return AspectPoolAccess.amount(player, holder(player, aspect));
    }

    public static boolean hasDiscoveredComponents(Player player, ResourceLocation aspect) {
        return AspectPoolAccess.hasDiscoveredComponents(player, holder(player, aspect));
    }

    public static int grant(ServerPlayer player, ResourceLocation aspect, int amount) {
        return AspectPoolAccess.grant(player, holder(player, aspect), amount);
    }

    public static void grantAll(ServerPlayer player, AspectList aspects) {
        AspectPoolAccess.grantAll(player, aspects);
    }

    public static boolean spend(ServerPlayer player, ResourceLocation aspect, int amount) {
        return AspectPoolAccess.spend(player, holder(player, aspect), amount);
    }

    public static boolean canAfford(Player player, AspectList cost) {
        return AspectPoolAccess.canAfford(player, cost);
    }

    public static boolean spendAll(ServerPlayer player, AspectList cost) {
        return AspectPoolAccess.spendAll(player, cost);
    }

    public static void refund(ServerPlayer player, ResourceLocation aspect, int amount) {
        AspectPoolAccess.refund(player, holder(player, aspect), amount);
    }
}
