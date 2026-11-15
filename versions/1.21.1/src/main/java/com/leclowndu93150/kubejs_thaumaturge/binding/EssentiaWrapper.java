package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaAccess;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCrystalAccess;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaTransfer;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaItemStorage;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaStorage;
import com.leclowndu93150.thaumaturge.api.essentia.IItemEssentia;
import com.leclowndu93150.thaumaturge.api.essentia.ItemEssentiaTransferResult;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public final class EssentiaWrapper {
    private EssentiaWrapper() {}

    private static Holder<IAspect> aspect(HolderLookup.Provider registries, ResourceLocation id) {
        return registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(ResourceKey.create(IAspect.REGISTRY_KEY, id));
    }

    private static @Nullable ResourceLocation id(@Nullable Holder<IAspect> aspect) {
        return aspect == null
                ? null
                : aspect.unwrapKey().map(ResourceKey::location).orElse(null);
    }

    public static boolean isTransport(Level level, BlockPos pos, Direction face) {
        return EssentiaAccess.hasConduit(level, pos, face);
    }

    public static boolean isStorage(Level level, BlockPos pos, @Nullable Direction face) {
        return level.getCapability(EssentiaCapabilities.STORAGE, pos, face) != null;
    }

    public static AspectList contents(Level level, BlockPos pos, @Nullable Direction face) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        return storage == null ? AspectList.EMPTY : storage.contents();
    }

    public static int amount(Level level, BlockPos pos, @Nullable Direction face, ResourceLocation aspect) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        return storage == null ? 0 : storage.amount(aspect(level.registryAccess(), aspect));
    }

    public static int insert(Level level, BlockPos pos, @Nullable Direction face, ResourceLocation aspect, int amount) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        if (storage == null || amount <= 0) {
            return 0;
        }
        return storage.insert(aspect(level.registryAccess(), aspect), amount, false);
    }

    public static int extract(
            Level level, BlockPos pos, @Nullable Direction face, ResourceLocation aspect, int amount) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        if (storage == null || amount <= 0) {
            return 0;
        }
        return storage.extract(aspect(level.registryAccess(), aspect), amount, false);
    }

    public static int transfer(
            Level level,
            BlockPos from,
            Direction fromFace,
            BlockPos to,
            Direction toFace,
            ResourceLocation aspect,
            int max) {
        return EssentiaTransfer.transfer(
                level, from, fromFace, to, toFace, aspect(level.registryAccess(), aspect), max, false);
    }

    public static AspectList itemContents(ItemStack stack) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (storage != null) {
            return storage.contents();
        }
        IItemEssentia essentia = stack.getCapability(EssentiaCapabilities.CONTAINER);
        return essentia == null ? AspectList.EMPTY : essentia.getAspects();
    }

    public static int itemCapacity(ItemStack stack, ResourceLocation aspect) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        return storage == null ? 0 : storage.capacity(aspect(RegistryAccessContainer.current.access(), aspect));
    }

    public static ItemEssentiaTransferResult fillItem(ItemStack stack, ResourceLocation aspect, int amount) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (storage == null || amount <= 0) {
            return new ItemEssentiaTransferResult(0, stack);
        }
        return storage.insert(aspect(RegistryAccessContainer.current.access(), aspect), amount);
    }

    public static ItemEssentiaTransferResult drainItem(ItemStack stack, ResourceLocation aspect, int amount) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (storage == null || amount <= 0) {
            return new ItemEssentiaTransferResult(0, stack);
        }
        return storage.extract(aspect(RegistryAccessContainer.current.access(), aspect), amount);
    }

    public static ItemStack crystal(ResourceLocation aspect, int count) {
        return EssentiaCrystalAccess.create(aspect(RegistryAccessContainer.current.access(), aspect), count);
    }

    public static @Nullable ResourceLocation crystalAspect(ItemStack stack) {
        return id(EssentiaCrystalAccess.aspect(stack));
    }

    public static @Nullable ResourceLocation aspectFilter(ItemStack stack) {
        ResourceKey<IAspect> filter = EssentiaAccess.aspectFilter(stack);
        return filter == null ? null : filter.location();
    }

    public static ItemStack withAspectFilter(ItemStack stack, @Nullable ResourceLocation aspect) {
        return EssentiaAccess.withAspectFilter(
                stack, aspect == null ? null : ResourceKey.create(IAspect.REGISTRY_KEY, aspect));
    }
}
