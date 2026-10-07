package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.event.ScriptedSpellEvent;
import net.minecraft.resources.Identifier;

public final class ScriptedSpellKubeEvent extends SpellContextKubeEvent {
    private final ScriptedSpellEvent event;
    private final SpellTargetList targets;

    public ScriptedSpellKubeEvent(ScriptedSpellEvent event) {
        super(event.context());
        this.event = event;
        this.targets = new SpellTargetList(event.targets());
    }

    public String getKey() {
        return event.key();
    }

    public float getPower() {
        return event.power();
    }

    public SpellTargetList getTargets() {
        return targets;
    }

    @Override
    public float getStat(Identifier id) {
        return event.state().get(statOf(id));
    }

    public void setStat(Identifier id, float value) {
        event.setState(event.state().with(statOf(id), value));
    }

    public boolean hasNext() {
        return context.hasNext();
    }

    public void stop() {
        event.stop();
    }

    public void proceedLater(int ticks) {
        context.proceedLater(Math.max(1, ticks), event.targets(), event.state());
        event.stop();
    }
}
