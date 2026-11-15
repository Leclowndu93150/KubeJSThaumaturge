package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.capability.PlayerResearch;
import com.leclowndu93150.thaumaturge.api.capability.PlayerResearchAccess;
import com.leclowndu93150.thaumaturge.api.capability.ResearchFlag;
import com.leclowndu93150.thaumaturge.api.capability.ResearchStatus;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.IResearchEntry;
import com.leclowndu93150.thaumaturge.content.research.ResearchGrants;
import com.leclowndu93150.thaumaturge.content.research.ResearchManager;
import com.leclowndu93150.thaumaturge.content.research.ResearchProgression;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.rhino.Context;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class ResearchWrapper {
    private ResearchWrapper() {}

    private static @Nullable ResourceKey<IResearchCategory> categoryKey(@Nullable ResourceLocation category) {
        return category == null ? null : ResourceKey.create(IResearchCategory.REGISTRY_KEY, category);
    }

    private static @Nullable Holder<IResearchCategory> categoryHolder(
            Player player, @Nullable ResourceLocation category) {
        return category == null
                ? null
                : player.registryAccess()
                        .lookupOrThrow(IResearchCategory.REGISTRY_KEY)
                        .getOrThrow(categoryKey(category));
    }

    public static ResearchStatus status(Player player, ResourceLocation research) {
        return PlayerResearchAccess.of(player).status(research);
    }

    public static boolean isKnown(Player player, ResourceLocation research) {
        return PlayerResearchAccess.of(player).knows(research);
    }

    public static boolean isComplete(Player player, ResourceLocation research) {
        return PlayerResearchAccess.of(player).hasCompleted(research);
    }

    public static int stage(Player player, ResourceLocation research) {
        return PlayerResearchAccess.of(player).stageOf(research);
    }

    public static boolean hasReachedStage(Player player, ResourceLocation research, int stage) {
        return PlayerResearchAccess.of(player).hasReachedStage(research, stage);
    }

    public static int stageCount(Player player, ResourceLocation research) {
        return player.registryAccess()
                .lookupOrThrow(IResearchEntry.REGISTRY_KEY)
                .get(ResourceKey.create(IResearchEntry.REGISTRY_KEY, research))
                .map(entry -> entry.value().stages().size())
                .orElse(0);
    }

    public static List<ResourceLocation> known(Player player) {
        return List.copyOf(PlayerResearchAccess.of(player).knownEntries());
    }

    public static List<ResourceLocation> completed(Player player) {
        PlayerResearch research = PlayerResearchAccess.of(player);
        return research.knownEntries().stream().filter(research::hasCompleted).toList();
    }

    public static boolean unlock(ServerPlayer player, ResourceLocation research) {
        return ResearchManager.unlock(player, research);
    }

    public static boolean advance(ServerPlayer player, ResourceLocation research) {
        return ResearchManager.advanceStage(player, research, false);
    }

    public static boolean advance(ServerPlayer player, ResourceLocation research, boolean checkRequirements) {
        return ResearchManager.advanceStage(player, research, checkRequirements);
    }

    public static boolean setStage(ServerPlayer player, ResourceLocation research, int stage) {
        return ResearchManager.setStage(player, research, stage);
    }

    public static boolean complete(ServerPlayer player, ResourceLocation research) {
        return ResearchManager.complete(player, research);
    }

    public static boolean forget(ServerPlayer player, ResourceLocation research) {
        PlayerResearch state = PlayerResearchAccess.of(player);
        boolean forgotten = state.forget(research);
        if (forgotten) {
            state.queueSync(player);
        }
        return forgotten;
    }

    public static int grant(ServerPlayer player, ResourceLocation research) {
        return ResearchProgression.grant(player, research);
    }

    public static int prepare(ServerPlayer player, ResourceLocation research) {
        return ResearchProgression.prepare(player, research);
    }

    public static int revoke(ServerPlayer player, ResourceLocation research) {
        return ResearchProgression.revoke(player, research);
    }

    public static int completeCategory(ServerPlayer player, ResourceLocation category) {
        return ResearchProgression.completeCategory(player, categoryKey(category));
    }

    public static int grantAll(ServerPlayer player) {
        return ResearchGrants.grantAll(player);
    }

    public static void reset(ServerPlayer player) {
        ResearchProgression.reset(player);
    }

    public static boolean hasFlag(Player player, ResourceLocation research, ResearchFlag flag) {
        return PlayerResearchAccess.of(player).hasFlag(research, flag);
    }

    public static boolean addFlag(ServerPlayer player, ResourceLocation research, ResearchFlag flag) {
        PlayerResearch state = PlayerResearchAccess.of(player);
        boolean changed = state.addFlag(research, flag);
        if (changed) {
            state.queueSync(player);
        }
        return changed;
    }

    public static boolean removeFlag(ServerPlayer player, ResourceLocation research, ResearchFlag flag) {
        PlayerResearch state = PlayerResearchAccess.of(player);
        boolean changed = state.removeFlag(research, flag);
        if (changed) {
            state.queueSync(player);
        }
        return changed;
    }

    public static int knowledge(Player player, KnowledgeType type, @Nullable ResourceLocation category) {
        return PlayerResearchAccess.of(player).knowledgeLevel(type, categoryKey(category));
    }

    public static int knowledgePoints(Player player, KnowledgeType type, @Nullable ResourceLocation category) {
        return PlayerResearchAccess.of(player).knowledgePoints(type, categoryKey(category));
    }

    public static boolean addKnowledge(
            ServerPlayer player, KnowledgeType type, @Nullable ResourceLocation category, int amount) {
        return ResearchManager.gainKnowledge(player, type, categoryHolder(player, category), amount);
    }

    public static void require(Context cx, ResourceLocation research, ResearchRequirements.Requirement requirement) {
        require(cx, research, requirement, null);
    }

    public static void require(
            Context cx,
            ResourceLocation research,
            ResearchRequirements.Requirement requirement,
            @Nullable Component lockedMessage) {
        ResearchRequirements.require(((KubeJSContext) cx).getType(), research, requirement, lockedMessage);
    }
}
