package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.IGolemAPI;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemArm;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import java.util.function.BiConsumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public class GolemArmBuilder extends GolemPartBuilder<GolemArm> {
    public static final ResourceKey<Registry<GolemArm>> REGISTRY = GolemArm.REGISTRY_KEY;

    private @Nullable BiConsumer<IGolemAPI, Entity> onMeleeHit;
    private @Nullable GolemRangedAttack onRangedAttack;
    private int rangedMinInterval = ScriptedArmAbility.DEFAULT_MIN_INTERVAL;
    private int rangedMaxInterval = ScriptedArmAbility.DEFAULT_MAX_INTERVAL;
    private float rangedRange = ScriptedArmAbility.DEFAULT_RANGE;

    public GolemArmBuilder(ResourceLocation id) {
        super(id, GolemPartModel.AttachPoint.ARMS);
    }

    @Override
    protected String langKind() {
        return "arm";
    }

    public GolemArmBuilder onMeleeHit(BiConsumer<IGolemAPI, Entity> callback) {
        this.onMeleeHit = callback;
        return this;
    }

    public GolemArmBuilder onRangedAttack(GolemRangedAttack callback) {
        this.onRangedAttack = callback;
        return this;
    }

    public GolemArmBuilder rangedTiming(int minInterval, int maxInterval, float range) {
        this.rangedMinInterval = minInterval;
        this.rangedMaxInterval = maxInterval;
        this.rangedRange = range;
        return this;
    }

    @Override
    public GolemArm createObject() {
        ScriptedArmAbility ability = onTick == null && onMeleeHit == null && onRangedAttack == null
                ? null
                : new ScriptedArmAbility(
                        onTick, onMeleeHit, onRangedAttack, rangedMinInterval, rangedMaxInterval, rangedRange);
        return new GolemArm(research, icon, partModel(), components, ability, resolvedTraits());
    }
}
