package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandAccess;
import com.leclowndu93150.thaumaturge.api.wands.WandVis;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class WandVisWrapper {
    private WandVisWrapper() {}

    private static ResourceKey<IAspect> key(ResourceLocation aspect) {
        return ResourceKey.create(IAspect.REGISTRY_KEY, aspect);
    }

    public static WandVis getAllVis(ItemStack wand) {
        return WandAccess.getAllVis(wand);
    }

    public static int getVis(ItemStack wand, ResourceLocation aspect) {
        return WandAccess.getVis(wand, key(aspect));
    }

    public static ItemStack withVis(ItemStack wand, ResourceLocation aspect, int centivis) {
        return WandAccess.withVis(wand, key(aspect), centivis);
    }

    public static int setVis(ItemStack wand, ResourceLocation aspect, int centivis) {
        wand.applyComponents(withVis(wand, aspect, centivis).getComponentsPatch());
        return getVis(wand, aspect);
    }

    public static int addVis(ItemStack wand, ResourceLocation aspect, int centivis) {
        return setVis(wand, aspect, Math.max(0, getVis(wand, aspect) + centivis));
    }
}
