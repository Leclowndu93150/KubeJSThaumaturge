package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class SpellTargetWrapper {
    private final SpellTarget target;

    public SpellTargetWrapper(SpellTarget target) {
        this.target = target;
    }

    public static SpellTargetWrapper ofEntity(Entity entity, Vec3 direction) {
        return new SpellTargetWrapper(SpellTarget.entity(entity, direction));
    }

    public SpellTarget unwrap() {
        return target;
    }

    public @Nullable Entity getEntity() {
        return target.entity().orElse(null);
    }

    public @Nullable BlockPos getBlockPos() {
        return target.block().map(BlockHitResult::getBlockPos).orElse(null);
    }

    public @Nullable Direction getFace() {
        return target.block().map(BlockHitResult::getDirection).orElse(null);
    }

    public Vec3 getPosition() {
        return target.position();
    }

    public Vec3 getDirection() {
        return target.direction();
    }

    public boolean isEntity() {
        return target.entity().isPresent();
    }

    public boolean isBlock() {
        return target.block().isPresent();
    }

    public boolean isMiss() {
        return target.hit().getType() == HitResult.Type.MISS;
    }
}
