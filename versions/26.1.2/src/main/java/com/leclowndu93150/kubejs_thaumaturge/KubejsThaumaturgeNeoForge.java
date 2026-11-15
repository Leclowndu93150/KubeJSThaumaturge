package com.leclowndu93150.kubejs_thaumaturge;

import com.leclowndu93150.kubejs_thaumaturge.client.KubeJSThaumaturgeClient;
import com.leclowndu93150.kubejs_thaumaturge.event.ThaumaturgeEventBridge;
import com.leclowndu93150.kubejs_thaumaturge.event.spell.SpellEventBridge;
import com.leclowndu93150.kubejs_thaumaturge.registry.CustomJarBlockEntities;
import com.leclowndu93150.kubejs_thaumaturge.registry.KubeJSThaumaturgeCapabilities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod("kubejs_thaumaturge")
public class KubejsThaumaturgeNeoForge {
    public KubejsThaumaturgeNeoForge(IEventBus modBus) {
        ThaumaturgeEventBridge.register(NeoForge.EVENT_BUS);
        SpellEventBridge.register(NeoForge.EVENT_BUS);
        KubeJSThaumaturgeCapabilities.register(modBus);
        CustomJarBlockEntities.register(modBus);
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            KubeJSThaumaturgeClient.register(modBus);
        }
    }
}
