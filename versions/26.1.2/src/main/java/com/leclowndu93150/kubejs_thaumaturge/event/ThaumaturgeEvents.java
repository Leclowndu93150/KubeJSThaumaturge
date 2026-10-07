package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.kubejs_thaumaturge.event.spell.ScriptedSpellKubeEvent;
import com.leclowndu93150.kubejs_thaumaturge.event.spell.SpellCastKubeEvent;
import com.leclowndu93150.kubejs_thaumaturge.event.spell.SpellEffectKubeEvent;
import com.leclowndu93150.kubejs_thaumaturge.event.spell.SpellInscribeKubeEvent;
import com.leclowndu93150.kubejs_thaumaturge.event.spell.SpellNodeKubeEvent;
import com.leclowndu93150.kubejs_thaumaturge.event.spell.SpellPreCastKubeEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;

public interface ThaumaturgeEvents {
    EventGroup GROUP = EventGroup.of("ThaumaturgeEvents");

    EventHandler RESEARCH_UNLOCKED =
            GROUP.server("researchUnlocked", () -> ResearchKubeEvent.class).hasResult();
    EventHandler RESEARCH_COMPLETED =
            GROUP.server("researchCompleted", () -> ResearchKubeEvent.class).hasResult();
    EventHandler STAGE_ADVANCED = GROUP.server("researchStageAdvanced", () -> StageAdvancedKubeEvent.class)
            .hasResult();
    EventHandler KNOWLEDGE_GAINED = GROUP.server("knowledgeGained", () -> KnowledgeGainedKubeEvent.class)
            .hasResult();
    EventHandler INFUSION_CRAFTED = GROUP.server("infusionCrafted", () -> InfusionCraftedKubeEvent.class);
    EventHandler CRUCIBLE_CRAFTED = GROUP.server("crucibleCrafted", () -> CrucibleCraftedKubeEvent.class);
    EventHandler CRUCIBLE_DECOMPOSE = GROUP.server("crucibleDecompose", () -> CrucibleDecomposeKubeEvent.class)
            .hasResult();
    EventHandler ARCANE_CRAFT_COST = GROUP.common("arcaneCraftCost", () -> ArcaneCraftCostKubeEvent.class);
    EventHandler SPELL_CAST =
            GROUP.server("spellCast", () -> SpellPreCastKubeEvent.class).hasResult();
    EventHandler SPELL_CASTED = GROUP.server("spellCasted", () -> SpellCastKubeEvent.class);
    EventHandler SPELL_EFFECT =
            GROUP.server("spellEffect", () -> SpellEffectKubeEvent.class).hasResult();
    EventHandler SPELL_NODE = GROUP.server("spellNode", () -> SpellNodeKubeEvent.class).hasResult();
    TargetedEventHandler<String> SCRIPTED_SPELL = GROUP.server("scriptedSpell", () -> ScriptedSpellKubeEvent.class)
            .requiredTarget(EventTargetType.STRING);
    EventHandler SPELL_INSCRIBE =
            GROUP.server("spellInscribe", () -> SpellInscribeKubeEvent.class).hasResult();
}
