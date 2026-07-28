package com.leclowndu93150.kubejs_thaumaturge.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record ResearchKubeEvent(Player player, Identifier research) implements KubeEvent {}
