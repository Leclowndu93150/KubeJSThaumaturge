package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.WandAccess;
import com.leclowndu93150.thaumaturge.api.wands.WandVis;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

public final class WandVisWrapper {
    private WandVisWrapper() {}

    private static ResourceKey<IAspect> key(Identifier aspect) {
        return ResourceKey.create(IAspect.REGISTRY_KEY, aspect);
    }

    public static WandVis getAllVis(ItemStack wand) {
        return WandAccess.getAllVis(wand);
    }

    public static int getVis(ItemStack wand, Identifier aspect) {
        return WandAccess.getVis(wand, key(aspect));
    }

    public static ItemStack withVis(ItemStack wand, Identifier aspect, int centivis) {
        return WandAccess.withVis(wand, key(aspect), centivis);
    }

    public static int setVis(ItemStack wand, Identifier aspect, int centivis) {
        wand.applyComponents(withVis(wand, aspect, centivis).getComponentsPatch());
        return getVis(wand, aspect);
    }

    public static int addVis(ItemStack wand, Identifier aspect, int centivis) {
        return setVis(wand, aspect, Math.max(0, getVis(wand, aspect) + centivis));
    }
}
