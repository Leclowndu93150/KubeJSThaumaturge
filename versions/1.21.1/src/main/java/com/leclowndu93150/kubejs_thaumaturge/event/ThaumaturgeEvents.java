package com.leclowndu93150.kubejs_thaumaturge.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

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
}
