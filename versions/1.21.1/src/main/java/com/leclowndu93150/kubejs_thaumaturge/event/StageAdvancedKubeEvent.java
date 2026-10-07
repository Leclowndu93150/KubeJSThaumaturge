package com.leclowndu93150.kubejs_thaumaturge.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record StageAdvancedKubeEvent(Player player, ResourceLocation research, int previousStage, int newStage)
        implements KubeEvent {}
