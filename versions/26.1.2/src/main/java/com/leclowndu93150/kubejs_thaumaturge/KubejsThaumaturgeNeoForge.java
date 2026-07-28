package com.leclowndu93150.kubejs_thaumaturge;

import com.leclowndu93150.kubejs_thaumaturge.event.ThaumaturgeEventBridge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod("kubejs_thaumaturge")
public class KubejsThaumaturgeNeoForge {
    public KubejsThaumaturgeNeoForge(IEventBus modBus) {
        ThaumaturgeEventBridge.register(NeoForge.EVENT_BUS);
    }
}
