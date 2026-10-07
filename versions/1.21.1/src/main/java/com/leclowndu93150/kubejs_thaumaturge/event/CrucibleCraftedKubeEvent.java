package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.content.crucible.CrucibleEvent;
import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CrucibleCraftedKubeEvent implements KubePlayerEvent {
    private final CrucibleEvent.CrucibleCraftedEvent event;

    public CrucibleCraftedKubeEvent(CrucibleEvent.CrucibleCraftedEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.getPlayer();
    }

    public BlockPos getPos() {
        return event.getPos();
    }

    public ItemStack getResult() {
        return event.getCraftedStack().copy();
    }

    public AspectList getAspects() {
        return event.getConsumedAspects();
    }
}
