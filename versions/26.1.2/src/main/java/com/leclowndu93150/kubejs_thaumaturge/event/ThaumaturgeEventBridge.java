package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.research.ResearchEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.neoforged.bus.api.IEventBus;

public final class ThaumaturgeEventBridge {
    private ThaumaturgeEventBridge() {}

    public static void register(IEventBus bus) {
        bus.addListener(ThaumaturgeEventBridge::onUnlocked);
        bus.addListener(ThaumaturgeEventBridge::onCompleted);
        bus.addListener(ThaumaturgeEventBridge::onStageAdvanced);
    }

    private static void onUnlocked(ResearchEvent.Unlocked event) {
        ThaumaturgeEvents.RESEARCH_UNLOCKED
                .post(ScriptType.SERVER, new ResearchKubeEvent(event.player(), event.research()))
                .applyCancel(event);
    }

    private static void onCompleted(ResearchEvent.Completed event) {
        ThaumaturgeEvents.RESEARCH_COMPLETED
                .post(ScriptType.SERVER, new ResearchKubeEvent(event.player(), event.research()))
                .applyCancel(event);
    }

    private static void onStageAdvanced(ResearchEvent.StageAdvanced event) {
        ThaumaturgeEvents.STAGE_ADVANCED
                .post(
                        ScriptType.SERVER,
                        new StageAdvancedKubeEvent(
                                event.player(), event.research(), event.previousStage(), event.newStage()))
                .applyCancel(event);
    }
}
