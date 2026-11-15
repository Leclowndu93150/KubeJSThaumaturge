package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.cast.CastContext;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellStat;
import com.leclowndu93150.thaumaturge.api.spell.cast.SpellStats;
import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class SpellContextKubeEvent implements KubeLevelEvent {
    private static final float ARC_WIDTH = 0.5F;

    protected final CastContext context;

    protected SpellContextKubeEvent(CastContext context) {
        this.context = context;
    }

    @Override
    public Level getLevel() {
        return context.level();
    }

    public CastContext getContext() {
        return context;
    }

    public @Nullable LivingEntity getCaster() {
        return context.caster();
    }

    public UUID getCastId() {
        return context.castId();
    }

    public ResourceLocation getPart() {
        return context.partKey().location();
    }

    public @Nullable ResourceLocation getAspect() {
        return context.aspect()
                .flatMap(Holder::unwrapKey)
                .map(ResourceKey::location)
                .orElse(null);
    }

    public int getSetting(String key) {
        return context.setting(key);
    }

    public int getColor() {
        return context.color();
    }

    public float getStat(ResourceLocation id) {
        return stateStat(id);
    }

    public boolean isAlly(Entity entity) {
        return context.isAlly(entity);
    }

    public void impact(Vec3 at) {
        context.fx().impact(context.part().fx(), at, context.color());
    }

    public void burst(Vec3 at, Vec3 direction) {
        context.fx().burst(context.part().fx(), at, direction, context.color());
    }

    public void arc(Vec3 from, Vec3 to) {
        context.fx().arc(from, to, context.color(), ARC_WIDTH);
    }

    protected float stateStat(ResourceLocation id) {
        return context.state().get(statOf(id));
    }

    protected static SpellStat statOf(ResourceLocation id) {
        return SpellStats.byId(id).orElseGet(() -> new SpellStat(id, 0.0F));
    }
}
