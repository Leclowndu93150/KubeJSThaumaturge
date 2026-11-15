package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.wands.IWandVisStorage;
import com.leclowndu93150.thaumaturge.api.wands.WandVis;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record ScriptedWandVisStorage(Reader reader, Writer writer) implements IWandVisStorage {
    @FunctionalInterface
    public interface Reader {
        @Nullable
        Map<String, Number> read(ItemStack wand);
    }

    @FunctionalInterface
    public interface Writer {
        void write(ItemStack wand, Map<String, Integer> centivis);
    }

    @Override
    public WandVis getVis(ItemStack wand) {
        Map<String, Number> stored = reader.read(wand);
        if (stored == null || stored.isEmpty()) {
            return WandVis.EMPTY;
        }
        Map<ResourceKey<IAspect>, Integer> centivis = new LinkedHashMap<>();
        stored.forEach((aspect, amount) -> {
            int value = amount.intValue();
            if (value > 0) {
                centivis.put(ResourceKey.create(IAspect.REGISTRY_KEY, Identifier.parse(aspect)), value);
            }
        });
        return new WandVis(centivis);
    }

    @Override
    public void setVis(ItemStack wand, WandVis vis) {
        Map<String, Integer> centivis = new LinkedHashMap<>();
        vis.centivis()
                .forEach((aspect, amount) -> centivis.put(aspect.identifier().toString(), amount));
        writer.write(wand, centivis);
    }
}
