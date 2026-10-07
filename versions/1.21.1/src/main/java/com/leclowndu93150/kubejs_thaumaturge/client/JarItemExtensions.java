package com.leclowndu93150.kubejs_thaumaturge.client;

import com.leclowndu93150.thaumaturge.client.model.JarItemSpecialRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.util.Lazy;

public final class JarItemExtensions implements IClientItemExtensions {
    private final Lazy<BlockEntityWithoutLevelRenderer> renderer = Lazy.of(JarItemSpecialRenderer::new);

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer.get();
    }
}
