package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.Spell;
import com.leclowndu93150.thaumaturge.api.spell.SpellSummary;
import com.leclowndu93150.thaumaturge.api.spell.event.SpellInscribeEvent;
import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class SpellInscribeKubeEvent implements KubePlayerEvent {
    private final SpellInscribeEvent event;

    public SpellInscribeKubeEvent(SpellInscribeEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.player();
    }

    public BlockPos getPos() {
        return event.pos();
    }

    public ItemStack getFocus() {
        return event.focus();
    }

    public Spell getSpell() {
        return event.spell();
    }

    public SpellSummary getSummary() {
        return event.summary();
    }

    public int getComplexity() {
        return event.summary().complexity();
    }

    public int getXp() {
        return event.summary().xp();
    }
}
