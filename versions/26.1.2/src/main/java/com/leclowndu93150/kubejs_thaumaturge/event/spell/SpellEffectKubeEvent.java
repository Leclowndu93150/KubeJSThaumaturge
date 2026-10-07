package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.event.SpellEffectEvent;

public final class SpellEffectKubeEvent extends SpellContextKubeEvent {
    private final SpellEffectEvent event;
    private final SpellTargetWrapper target;

    public SpellEffectKubeEvent(SpellEffectEvent event) {
        super(event.context());
        this.event = event;
        this.target = new SpellTargetWrapper(event.target());
    }

    public SpellTargetWrapper getTarget() {
        return target;
    }

    public float getPower() {
        return event.power();
    }

    public void setPower(float power) {
        event.setPower(power);
    }
}
