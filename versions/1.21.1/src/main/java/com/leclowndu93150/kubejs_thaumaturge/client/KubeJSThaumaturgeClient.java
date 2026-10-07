package com.leclowndu93150.kubejs_thaumaturge.client;

import com.leclowndu93150.kubejs_thaumaturge.builder.GolemAccessoryItemBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.JarBlockBuilder;
import com.leclowndu93150.thaumaturge.api.client.golems.RegisterGolemAccessoryRenderersEvent;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryObjectStorage;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class KubeJSThaumaturgeClient {
    private KubeJSThaumaturgeClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(KubeJSThaumaturgeClient::onRegisterAccessoryRenderers);
        modBus.addListener(KubeJSThaumaturgeClient::onRegisterClientExtensions);
    }

    private static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        for (BuilderBase<?> builder : RegistryObjectStorage.ALL_BUILDERS) {
            if (builder instanceof JarBlockBuilder jar && jar.itemBuilder != null) {
                event.registerItem(new JarItemExtensions(), jar.itemBuilder.get());
            }
        }
    }

    private static void onRegisterAccessoryRenderers(RegisterGolemAccessoryRenderersEvent event) {
        for (BuilderBase<?> builder : RegistryObjectStorage.ALL_BUILDERS) {
            if (builder instanceof GolemAccessoryItemBuilder accessory
                    && accessory.accessory != null
                    && accessory.renderAnchor != null) {
                event.register(accessory.accessory, accessory.renderAnchor, new GolemAccessoryItemRenderer(accessory));
            }
        }
    }
}
