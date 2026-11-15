package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import java.util.Map;
import net.minecraft.resources.Identifier;

public final class DataMapsWrapper {
    private DataMapsWrapper() {}

    public static void aspect(KubeDataGenerator event, Identifier id, JsonObject json) {
        event.json(Identifier.fromNamespaceAndPath(id.getNamespace(), "thaumaturge/aspect/" + id.getPath()), json);
    }

    public static void itemAspects(KubeDataGenerator event, Map<String, JsonElement> entries) {
        write(event, "item", "base_aspects", entries);
    }

    public static void entityAspects(KubeDataGenerator event, Map<String, JsonElement> entries) {
        write(event, "entity_type", "entity_aspects", entries);
    }

    public static void itemWarp(KubeDataGenerator event, Map<String, Integer> entries) {
        JsonObject values = new JsonObject();
        entries.forEach((item, amount) -> {
            JsonObject warp = new JsonObject();
            warp.addProperty("amount", amount);
            values.add(item, warp);
        });
        JsonObject file = new JsonObject();
        file.add("values", values);
        event.json(Identifier.fromNamespaceAndPath("thaumaturge", "data_maps/item/warp"), file);
    }

    public static void taintedProfile(KubeDataGenerator event, Map<String, JsonElement> entries) {
        write(event, "entity_type", "tainted_profile", entries);
    }

    public static void trimBonus(KubeDataGenerator event, Map<String, JsonElement> entries) {
        write(event, "trim_material", "trim_bonus", entries);
    }

    public static void spellFocus(KubeDataGenerator event, Map<String, JsonElement> entries) {
        write(event, "item", "spell_focus", entries);
    }

    public static void smelterStats(KubeDataGenerator event, Map<String, JsonElement> entries) {
        write(event, "block", "smelter_stats", entries);
    }

    private static void write(KubeDataGenerator event, String kind, String name, Map<String, JsonElement> entries) {
        JsonObject values = new JsonObject();
        entries.forEach(values::add);
        JsonObject file = new JsonObject();
        file.add("values", values);
        event.json(Identifier.fromNamespaceAndPath("thaumaturge", "data_maps/" + kind + "/" + name), file);
    }
}
