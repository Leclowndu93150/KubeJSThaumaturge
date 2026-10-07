package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aura.VisRelayHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;

public final class VisRelayWrapper {
    private VisRelayWrapper() {}

    public static int drain(ServerLevel level, BlockPos pos, Identifier primal, int centivis) {
        return VisRelayHelper.drainCentivis(
                level, pos, ResourceKey.create(IAspect.REGISTRY_KEY, primal), centivis, false);
    }

    public static int available(ServerLevel level, BlockPos pos, Identifier primal, int centivis) {
        return VisRelayHelper.drainCentivis(
                level, pos, ResourceKey.create(IAspect.REGISTRY_KEY, primal), centivis, true);
    }
}
