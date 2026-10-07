package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.event.SpellNodeEvent;

public final class SpellNodeKubeEvent extends SpellContextKubeEvent {
    private final SpellTargetList targets;

    public SpellNodeKubeEvent(SpellNodeEvent event) {
        super(event.context());
        this.targets = new SpellTargetList(event.targets());
    }

    public SpellTargetList getTargets() {
        return targets;
    }
}
