package com.leclowndu93150.kubejs_thaumaturge.builder;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.function.Supplier;
import net.minecraft.resources.Identifier;

public class WandPartEntryBuilder<T> extends BuilderBase<T> {
    private final Supplier<T> factory;

    public WandPartEntryBuilder(Identifier id, Supplier<T> factory) {
        super(id);
        this.factory = factory;
    }

    @Override
    public T createObject() {
        return factory.get();
    }
}
