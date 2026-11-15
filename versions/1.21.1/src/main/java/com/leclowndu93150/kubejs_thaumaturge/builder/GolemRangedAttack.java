package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.IGolemAPI;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface GolemRangedAttack {
    void attack(IGolemAPI golem, LivingEntity target, float power);
}
