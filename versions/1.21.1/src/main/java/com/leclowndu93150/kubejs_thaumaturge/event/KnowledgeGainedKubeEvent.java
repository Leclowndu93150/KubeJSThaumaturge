package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.ResearchEvent;
import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public class KnowledgeGainedKubeEvent implements KubePlayerEvent {
    private final ResearchEvent.KnowledgeGained event;

    public KnowledgeGainedKubeEvent(ResearchEvent.KnowledgeGained event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.player();
    }

    public KnowledgeType getType() {
        return event.type();
    }

    public @Nullable ResourceLocation getCategory() {
        return event.category() == null
                ? null
                : event.category().unwrapKey().map(key -> key.location()).orElse(null);
    }

    public int getAmount() {
        return event.amount();
    }
}
