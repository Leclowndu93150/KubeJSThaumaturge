package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.resources.ResourceLocation;

public class CodecRegistryBuilder<T> extends BuilderBase<T> {
    private final Codec<? extends T> codec;
    private JsonElement json;

    public CodecRegistryBuilder(ResourceLocation id, Codec<? extends T> codec) {
        super(id);
        this.codec = codec;
        this.json = new JsonObject();
    }

    public JsonObject json() {
        if (json instanceof JsonObject object) {
            return object;
        }
        JsonObject object = new JsonObject();
        json = object;
        return object;
    }

    public CodecRegistryBuilder<T> json(JsonElement json) {
        this.json = json.deepCopy();
        return this;
    }

    public CodecRegistryBuilder<T> merge(JsonObject json) {
        JsonObject current = json();
        for (var entry : json.entrySet()) {
            current.add(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public T createObject() {
        DataResult<? extends T> result = codec.parse(RegistryAccessContainer.current.json(), json);
        if (result.result().isPresent()) {
            return result.result().get();
        }
        String registry = registryKey == null
                ? "<unknown registry>"
                : registryKey.location().toString();
        String message = result.error().map(DataResult.Error::message).orElse("Unknown error");
        throw new KubeRuntimeException(
                        "Failed to parse registry object '%s' of '%s': %s".formatted(id, registry, message))
                .source(sourceLine);
    }
}
