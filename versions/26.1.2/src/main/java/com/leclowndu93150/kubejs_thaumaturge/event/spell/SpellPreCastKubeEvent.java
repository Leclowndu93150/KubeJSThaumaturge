package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.event.SpellCastEvent;

public final class SpellPreCastKubeEvent extends SpellCastKubeEvent {
    private final SpellCastEvent.Pre pre;

    public SpellPreCastKubeEvent(SpellCastEvent.Pre pre) {
        super(pre);
        this.pre = pre;
    }

    public float getPower() {
        return pre.power();
    }

    public void setPower(float power) {
        pre.setPower(power);
    }

    public float getVisCost() {
        return pre.visCost();
    }

    public void setVisCost(float visCost) {
        pre.setVisCost(visCost);
    }
}
