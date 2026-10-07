package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.infusion.InfusionCraftedEvent;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftCostEvent;
import com.leclowndu93150.thaumaturge.api.research.ResearchEvent;
import com.leclowndu93150.thaumaturge.content.crucible.CrucibleEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.neoforged.bus.api.IEventBus;

public final class ThaumaturgeEventBridge {
    private ThaumaturgeEventBridge() {}

    public static void register(IEventBus bus) {
        bus.addListener(ThaumaturgeEventBridge::onUnlocked);
        bus.addListener(ThaumaturgeEventBridge::onCompleted);
        bus.addListener(ThaumaturgeEventBridge::onStageAdvanced);
        bus.addListener(ThaumaturgeEventBridge::onKnowledgeGained);
        bus.addListener(ThaumaturgeEventBridge::onInfusionCrafted);
        bus.addListener(ThaumaturgeEventBridge::onCrucibleCrafted);
        bus.addListener(ThaumaturgeEventBridge::onCrucibleDecompose);
        bus.addListener(ThaumaturgeEventBridge::onArcaneCraftCost);
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

    private static void onKnowledgeGained(ResearchEvent.KnowledgeGained event) {
        ThaumaturgeEvents.KNOWLEDGE_GAINED
                .post(ScriptType.SERVER, new KnowledgeGainedKubeEvent(event))
                .applyCancel(event);
    }

    private static void onInfusionCrafted(InfusionCraftedEvent event) {
        ThaumaturgeEvents.INFUSION_CRAFTED.post(ScriptType.SERVER, new InfusionCraftedKubeEvent(event));
    }

    private static void onCrucibleCrafted(CrucibleEvent.CrucibleCraftedEvent event) {
        ThaumaturgeEvents.CRUCIBLE_CRAFTED.post(ScriptType.SERVER, new CrucibleCraftedKubeEvent(event));
    }

    private static void onCrucibleDecompose(CrucibleEvent.CrucibleDecomposeItemEvent event) {
        ThaumaturgeEvents.CRUCIBLE_DECOMPOSE
                .post(ScriptType.SERVER, new CrucibleDecomposeKubeEvent(event))
                .applyCancel(event);
    }

    private static void onArcaneCraftCost(ArcaneCraftCostEvent event) {
        ScriptType type = event.getPlayer().level().isClientSide() ? ScriptType.CLIENT : ScriptType.SERVER;
        ThaumaturgeEvents.ARCANE_CRAFT_COST.post(type, new ArcaneCraftCostKubeEvent(event));
    }
}
