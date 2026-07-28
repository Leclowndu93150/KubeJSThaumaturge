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
}
