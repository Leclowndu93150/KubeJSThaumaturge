package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaAccess;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCrystalAccess;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaTransfer;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaContainerItem;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaItemStorage;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaStorage;
import com.leclowndu93150.thaumaturge.api.essentia.ItemEssentiaTransferResult;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

public final class EssentiaWrapper {
    private EssentiaWrapper() {}

    private static Holder<IAspect> aspect(HolderLookup.Provider registries, Identifier id) {
        return registries.lookupOrThrow(IAspect.REGISTRY_KEY).getOrThrow(ResourceKey.create(IAspect.REGISTRY_KEY, id));
    }

    private static @Nullable Identifier id(@Nullable Holder<IAspect> aspect) {
        return aspect == null
                ? null
                : aspect.unwrapKey().map(ResourceKey::identifier).orElse(null);
    }

    public static boolean isTransport(Level level, BlockPos pos, Direction face) {
        return EssentiaAccess.isEssentiaTransport(level, pos, face);
    }

    public static boolean isStorage(Level level, BlockPos pos, @Nullable Direction face) {
        return level.getCapability(EssentiaCapabilities.STORAGE, pos, face) != null;
    }

    public static AspectList contents(Level level, BlockPos pos, @Nullable Direction face) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        return storage == null ? AspectList.EMPTY : storage.contents();
    }

    public static int amount(Level level, BlockPos pos, @Nullable Direction face, Identifier aspect) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        return storage == null ? 0 : storage.amount(aspect(level.registryAccess(), aspect));
    }

    public static int insert(Level level, BlockPos pos, @Nullable Direction face, Identifier aspect, int amount) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        if (storage == null || amount <= 0) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = storage.insert(aspect(level.registryAccess(), aspect), amount, transaction);
            transaction.commit();
            return inserted;
        }
    }

    public static int extract(Level level, BlockPos pos, @Nullable Direction face, Identifier aspect, int amount) {
        IEssentiaStorage storage = level.getCapability(EssentiaCapabilities.STORAGE, pos, face);
        if (storage == null || amount <= 0) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int extracted = storage.extract(aspect(level.registryAccess(), aspect), amount, transaction);
            transaction.commit();
            return extracted;
        }
    }

    public static int transfer(
            Level level, BlockPos from, Direction fromFace, BlockPos to, Direction toFace, Identifier aspect, int max) {
        return EssentiaTransfer.transfer(
                level, from, fromFace, to, toFace, aspect(level.registryAccess(), aspect), max, false);
    }

    public static AspectList itemContents(ItemStack stack) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (storage != null) {
            return storage.contents();
        }
        IEssentiaContainerItem container = stack.getCapability(EssentiaCapabilities.CONTAINER);
        return container == null ? AspectList.EMPTY : container.getAspects(stack);
    }

    public static int itemCapacity(ItemStack stack, Identifier aspect) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        return storage == null ? 0 : storage.capacity(aspect(RegistryAccessContainer.current, aspect));
    }

    public static ItemEssentiaTransferResult fillItem(ItemStack stack, Identifier aspect, int amount) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (storage == null || amount <= 0) {
            return new ItemEssentiaTransferResult(0, stack);
        }
        return storage.insert(aspect(RegistryAccessContainer.current, aspect), amount);
    }

    public static ItemEssentiaTransferResult drainItem(ItemStack stack, Identifier aspect, int amount) {
        IEssentiaItemStorage storage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (storage == null || amount <= 0) {
            return new ItemEssentiaTransferResult(0, stack);
        }
        return storage.extract(aspect(RegistryAccessContainer.current, aspect), amount);
    }

    public static ItemStack crystal(Identifier aspect, int count) {
        return EssentiaCrystalAccess.create(aspect(RegistryAccessContainer.current, aspect), count);
    }

    public static @Nullable Identifier crystalAspect(ItemStack stack) {
        return id(EssentiaCrystalAccess.aspect(stack));
    }

    public static @Nullable Identifier aspectFilter(ItemStack stack) {
        ResourceKey<IAspect> filter = EssentiaAccess.aspectFilter(stack);
        return filter == null ? null : filter.identifier();
    }

    public static ItemStack withAspectFilter(ItemStack stack, @Nullable Identifier aspect) {
        return EssentiaAccess.withAspectFilter(
                stack, aspect == null ? null : ResourceKey.create(IAspect.REGISTRY_KEY, aspect));
    }
}
