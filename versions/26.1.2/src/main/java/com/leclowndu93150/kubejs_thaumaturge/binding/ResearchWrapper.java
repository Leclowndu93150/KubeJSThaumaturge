package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.capability.IPlayerKnowledge;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeAccess;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
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
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class ResearchWrapper {
    private ResearchWrapper() {}

    private static @Nullable ResourceKey<IResearchCategory> categoryKey(@Nullable Identifier category) {
        return category == null ? null : ResourceKey.create(IResearchCategory.REGISTRY_KEY, category);
    }

    private static @Nullable Holder<IResearchCategory> categoryHolder(Player player, @Nullable Identifier category) {
        return category == null
                ? null
                : player.registryAccess()
                        .lookupOrThrow(IResearchCategory.REGISTRY_KEY)
                        .getOrThrow(categoryKey(category));
    }

    public static ResearchStatus status(Player player, Identifier research) {
        return KnowledgeAccess.of(player).researchStatus(research);
    }

    public static boolean isKnown(Player player, Identifier research) {
        return KnowledgeAccess.of(player).isResearchKnown(research);
    }

    public static boolean isComplete(Player player, Identifier research) {
        return KnowledgeAccess.of(player).isResearchComplete(research);
    }

    public static int stage(Player player, Identifier research) {
        return KnowledgeAccess.of(player).researchStage(research);
    }

    public static boolean hasReachedStage(Player player, Identifier research, int stage) {
        return KnowledgeAccess.of(player).isResearchKnown(research, stage);
    }

    public static int stageCount(Player player, Identifier research) {
        return player.registryAccess()
                .lookupOrThrow(IResearchEntry.REGISTRY_KEY)
                .get(ResourceKey.create(IResearchEntry.REGISTRY_KEY, research))
                .map(entry -> entry.value().stages().size())
                .orElse(0);
    }

    public static List<Identifier> known(Player player) {
        return List.copyOf(KnowledgeAccess.of(player).researchList());
    }

    public static List<Identifier> completed(Player player) {
        IPlayerKnowledge knowledge = KnowledgeAccess.of(player);
        return knowledge.researchList().stream()
                .filter(knowledge::isResearchComplete)
                .toList();
    }

    public static boolean unlock(ServerPlayer player, Identifier research) {
        return ResearchManager.unlock(player, research);
    }

    public static boolean advance(ServerPlayer player, Identifier research) {
        return ResearchManager.advanceStage(player, research, false);
    }

    public static boolean advance(ServerPlayer player, Identifier research, boolean checkRequirements) {
        return ResearchManager.advanceStage(player, research, checkRequirements);
    }

    public static boolean setStage(ServerPlayer player, Identifier research, int stage) {
        return ResearchManager.setStage(player, research, stage);
    }

    public static boolean complete(ServerPlayer player, Identifier research) {
        return ResearchManager.complete(player, research);
    }

    public static boolean forget(ServerPlayer player, Identifier research) {
        IPlayerKnowledge knowledge = KnowledgeAccess.of(player);
        boolean forgotten = knowledge.removeResearch(research);
        if (forgotten) {
            knowledge.sync(player);
        }
        return forgotten;
    }

    public static int grant(ServerPlayer player, Identifier research) {
        return ResearchProgression.grant(player, research);
    }

    public static int prepare(ServerPlayer player, Identifier research) {
        return ResearchProgression.prepare(player, research);
    }

    public static int revoke(ServerPlayer player, Identifier research) {
        return ResearchProgression.revoke(player, research);
    }

    public static int completeCategory(ServerPlayer player, Identifier category) {
        return ResearchProgression.completeCategory(player, categoryKey(category));
    }

    public static int grantAll(ServerPlayer player) {
        return ResearchGrants.grantAll(player);
    }

    public static void reset(ServerPlayer player) {
        ResearchProgression.reset(player);
    }

    public static boolean hasFlag(Player player, Identifier research, ResearchFlag flag) {
        return KnowledgeAccess.of(player).hasResearchFlag(research, flag);
    }

    public static boolean addFlag(ServerPlayer player, Identifier research, ResearchFlag flag) {
        IPlayerKnowledge knowledge = KnowledgeAccess.of(player);
        boolean changed = knowledge.setResearchFlag(research, flag);
        if (changed) {
            knowledge.sync(player);
        }
        return changed;
    }

    public static boolean removeFlag(ServerPlayer player, Identifier research, ResearchFlag flag) {
        IPlayerKnowledge knowledge = KnowledgeAccess.of(player);
        boolean changed = knowledge.clearResearchFlag(research, flag);
        if (changed) {
            knowledge.sync(player);
        }
        return changed;
    }

    public static int knowledge(Player player, KnowledgeType type, @Nullable Identifier category) {
        return KnowledgeAccess.of(player).knowledge(type, categoryKey(category));
    }

    public static int knowledgePoints(Player player, KnowledgeType type, @Nullable Identifier category) {
        return KnowledgeAccess.of(player).rawKnowledge(type, categoryKey(category));
    }

    public static boolean addKnowledge(
            ServerPlayer player, KnowledgeType type, @Nullable Identifier category, int amount) {
        return ResearchManager.gainKnowledge(player, type, categoryHolder(player, category), amount);
    }

    public static void require(Context cx, Identifier research, ResearchRequirements.Requirement requirement) {
        require(cx, research, requirement, null);
    }

    public static void require(
            Context cx,
            Identifier research,
            ResearchRequirements.Requirement requirement,
            @Nullable Component lockedMessage) {
        ResearchRequirements.require(((KubeJSContext) cx).getType(), research, requirement, lockedMessage);
    }
}
