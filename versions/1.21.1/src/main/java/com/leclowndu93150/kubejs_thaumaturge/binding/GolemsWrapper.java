package com.leclowndu93150.kubejs_thaumaturge.binding;

import com.leclowndu93150.thaumaturge.api.golems.GolemDispatch;
import com.leclowndu93150.thaumaturge.api.golems.SupplyOrder;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealEntity;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealPos;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealType;
import com.leclowndu93150.thaumaturge.api.golems.tasks.Task;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public final class GolemsWrapper {
    private GolemsWrapper() {}

    public static Optional<SealType> sealType(ResourceLocation id) {
        return GolemDispatch.sealType(id);
    }

    public static ItemStack getSealStack(ResourceLocation id) {
        return GolemDispatch.sealItem(id);
    }

    public static @Nullable ISealEntity getSealEntity(Level level, @Nullable SealPos pos) {
        return pos == null ? null : GolemDispatch.sealAt(level, pos).orElse(null);
    }

    public static void addGolemTask(Level level, Task task) {
        GolemDispatch.postTask(level, task);
    }

    public static void requestProvisioning(Level level, ISealEntity seal, ItemStack stack) {
        GolemDispatch.orderSupply(level, seal, stack);
    }

    public static void requestProvisioning(Level level, BlockPos pos, Direction side, ItemStack stack) {
        GolemDispatch.orderSupply(level, pos, side, stack);
    }

    public static void requestProvisioning(Level level, Entity entity, ItemStack stack) {
        GolemDispatch.orderSupply(level, entity, stack);
    }

    public static void requestProvisioning(Level level, BlockPos pos, Direction side, ItemStack stack, int batch) {
        GolemDispatch.orderSupply(level, pos, side, stack, batch);
    }

    public static void requestProvisioning(Level level, Entity entity, ItemStack stack, int batch) {
        GolemDispatch.orderSupply(level, entity, stack, batch);
    }

    public static List<SupplyOrder> getProvisionRequests(Level level) {
        return GolemDispatch.supplyQueue(level);
    }
}
