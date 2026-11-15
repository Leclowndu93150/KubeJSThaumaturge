package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.research.ResearchUnlockConditions;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class ResearchRequirements {
    private static final List<Registered> REGISTERED = new CopyOnWriteArrayList<>();

    private ResearchRequirements() {}

    @FunctionalInterface
    public interface Requirement {
        boolean isMet(Player player);
    }

    private static final class Registered {
        private final ScriptType scriptType;
        private final Requirement requirement;
        private volatile boolean active = true;

        private Registered(ScriptType scriptType, Requirement requirement) {
            this.scriptType = scriptType;
            this.requirement = requirement;
        }

        private boolean isMet(Player player) {
            return !active || requirement.isMet(player);
        }
    }

    public static void require(
            ScriptType scriptType, Identifier research, Requirement requirement, @Nullable Component lockedMessage) {
        Registered registered = new Registered(scriptType, requirement);
        REGISTERED.add(registered);
        ResearchUnlockConditions.register(research, (player, state) -> registered.isMet(player), lockedMessage);
    }

    public static void clear(ScriptType scriptType) {
        for (Registered registered : REGISTERED) {
            if (registered.scriptType == scriptType) {
                registered.active = false;
                REGISTERED.remove(registered);
            }
        }
    }
}
