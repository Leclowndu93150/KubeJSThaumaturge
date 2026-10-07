package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.infusion.InfusionCraftedEvent;
import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class InfusionCraftedKubeEvent implements KubeLevelEvent {
    private final InfusionCraftedEvent event;

    public InfusionCraftedKubeEvent(InfusionCraftedEvent event) {
        this.event = event;
    }

    @Override
    public ServerLevel getLevel() {
        return event.getLevel();
    }

    public BlockPos getPos() {
        return event.getMatrixPos();
    }

    public @Nullable ServerPlayer getPlayer() {
        return event.getPlayer();
    }

    public ItemStack getCatalyst() {
        return event.getCatalyst();
    }

    public ItemStack getResult() {
        return event.getResult();
    }

    public void setResult(ItemStack result) {
        event.setResult(result);
    }
}
