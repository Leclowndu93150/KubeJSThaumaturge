package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.thaumaturge.api.spell.CastStyle;
import com.leclowndu93150.thaumaturge.api.spell.Spell;
import com.leclowndu93150.thaumaturge.api.spell.event.SpellCastEvent;
import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpellCastKubeEvent implements KubeLivingEntityEvent {
    private final SpellCastEvent event;

    public SpellCastKubeEvent(SpellCastEvent event) {
        this.event = event;
    }

    @Override
    public LivingEntity getEntity() {
        return event.caster();
    }

    public ItemStack getFocus() {
        return event.focus();
    }

    public Spell getSpell() {
        return event.spell();
    }

    public CastStyle getStyle() {
        return event.style();
    }

    public String getStyleName() {
        return event.style().getSerializedName();
    }
}
