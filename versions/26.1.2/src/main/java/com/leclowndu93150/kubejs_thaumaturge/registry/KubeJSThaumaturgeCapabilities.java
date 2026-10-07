package com.leclowndu93150.kubejs_thaumaturge.registry;

import com.leclowndu93150.kubejs_thaumaturge.builder.JarBlockBuilder;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaContainerItem;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaJar;
import com.leclowndu93150.thaumaturge.content.essentia.storage.SingleAspectItemStorage;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryObjectStorage;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class KubeJSThaumaturgeCapabilities {
    private KubeJSThaumaturgeCapabilities() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(KubeJSThaumaturgeCapabilities::onRegisterCapabilities);
    }

    private static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        for (BuilderBase<?> builder : RegistryObjectStorage.ALL_BUILDERS) {
            if (builder instanceof JarBlockBuilder jar && jar.itemBuilder != null) {
                Item item = jar.itemBuilder.get();
                event.registerItem(
                        EssentiaCapabilities.CONTAINER, (stack, ctx) -> (IEssentiaContainerItem) stack.getItem(), item);
                event.registerItem(
                        EssentiaCapabilities.ITEM_STORAGE,
                        (stack, ctx) -> new SingleAspectItemStorage(
                                stack, (IEssentiaContainerItem) stack.getItem(), capacity(stack), false),
                        item);
            }
        }
    }

    private static int capacity(ItemStack stack) {
        return stack.getItem() instanceof BlockItem item && item.getBlock() instanceof IEssentiaJar jar
                ? jar.jarCapacity()
                : IEssentiaJar.DEFAULT_CAPACITY;
    }
}
