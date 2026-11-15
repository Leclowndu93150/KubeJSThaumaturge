package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.cast.SpellTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class SpellTargetList {
    private final List<SpellTarget> targets;

    public SpellTargetList(List<SpellTarget> targets) {
        this.targets = targets;
    }

    public List<SpellTargetWrapper> getAll() {
        List<SpellTargetWrapper> wrapped = new ArrayList<>(targets.size());
        for (SpellTarget target : targets) {
            wrapped.add(new SpellTargetWrapper(target));
        }
        return wrapped;
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        for (SpellTarget target : targets) {
            target.entity().ifPresent(entities::add);
        }
        return entities;
    }

    public int size() {
        return targets.size();
    }

    public void keepIf(Predicate<SpellTargetWrapper> filter) {
        targets.removeIf(target -> !filter.test(new SpellTargetWrapper(target)));
    }

    public void clear() {
        targets.clear();
    }

    public void addEntity(Entity entity, Vec3 direction) {
        targets.add(SpellTarget.entity(entity, direction));
    }

    public void add(SpellTargetWrapper target) {
        targets.add(target.unwrap());
    }
}
