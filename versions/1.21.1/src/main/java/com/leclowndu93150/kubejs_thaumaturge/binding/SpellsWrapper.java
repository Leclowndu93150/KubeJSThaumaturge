package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.spell.CastStyle;
import com.leclowndu93150.thaumaturge.api.spell.Spell;
import com.leclowndu93150.thaumaturge.api.spell.SpellNode;
import com.leclowndu93150.thaumaturge.api.spell.SpellSummary;
import com.leclowndu93150.thaumaturge.api.spell.Spells;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;
import java.util.List;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class SpellsWrapper {
    private SpellsWrapper() {}

    public static SpellNode node(ResourceLocation part) {
        return SpellNode.of(ResourceKey.create(SpellPart.REGISTRY_KEY, part));
    }

    public static SpellNode node(ResourceLocation part, ResourceLocation aspect) {
        return node(part).withAspect(Optional.of(ResourceKey.create(IAspect.REGISTRY_KEY, aspect)));
    }

    public static Spell spell(String style, SpellNode first) {
        return new Spell(styleNamed(style), SpellNode.of(Spell.ORIGIN).then(first));
    }

    public static Spell chain(String style, List<ResourceLocation> parts) {
        SpellNode chain = null;
        for (int index = parts.size() - 1; index >= 0; index--) {
            SpellNode node = node(parts.get(index));
            chain = chain == null ? node : node.then(chain);
        }
        return chain == null ? Spell.empty().withStyle(styleNamed(style)) : spell(style, chain);
    }

    public static void cast(LivingEntity caster, Spell spell, float power) {
        Spells.cast(caster, spell, power);
    }

    public static @Nullable Spell spellOf(ItemStack focus) {
        return Spells.spellOf(focus);
    }

    public static void setSpell(ItemStack focus, @Nullable Spell spell) {
        Spells.setSpell(focus, spell);
    }

    public static SpellSummary analyze(Player player, ItemStack focus, Spell spell) {
        return Spells.analyze(spell, Spells.tierOf(focus).orElse(null), player.registryAccess(), player);
    }

    private static CastStyle styleNamed(String name) {
        for (CastStyle style : CastStyle.values()) {
            if (style.getSerializedName().equals(name)) {
                return style;
            }
        }
        throw new IllegalArgumentException("Unknown cast style " + name);
    }
}
