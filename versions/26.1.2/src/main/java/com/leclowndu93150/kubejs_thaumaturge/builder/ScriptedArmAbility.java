package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.IGolemAPI;
import com.leclowndu93150.thaumaturge.api.golems.parts.IGolemArmAbility;
import com.leclowndu93150.thaumaturge.api.golems.parts.IGolemPartAbility;
import com.leclowndu93150.thaumaturge.content.golem.ai.VolleyGoal;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.jspecify.annotations.Nullable;

public record ScriptedArmAbility(
        @Nullable IGolemPartAbility onTick,
        @Nullable BiConsumer<IGolemAPI, Entity> onMeleeHit,
        @Nullable GolemRangedAttack onRangedAttack,
        int minInterval,
        int maxInterval,
        float range)
        implements IGolemArmAbility {
    public static final int DEFAULT_MIN_INTERVAL = 20;
    public static final int DEFAULT_MAX_INTERVAL = 25;
    public static final float DEFAULT_RANGE = 16.0F;
    private static final double ATTACK_MOVE_SPEED = 1.0;

    @Override
    public void tick(IGolemAPI golem) {
        if (onTick != null) {
            onTick.tick(golem);
        }
    }

    @Override
    public void onMeleeHit(IGolemAPI golem, Entity target) {
        if (onMeleeHit != null) {
            onMeleeHit.accept(golem, target);
        }
    }

    @Override
    public void onRangedAttack(IGolemAPI golem, LivingEntity target, float power) {
        if (onRangedAttack != null) {
            onRangedAttack.attack(golem, target, power);
        }
    }

    @Override
    public @Nullable Goal createRangedGoal(RangedAttackMob mob) {
        return onRangedAttack == null ? null : new VolleyGoal(mob, ATTACK_MOVE_SPEED, minInterval, maxInterval, range);
    }
}
