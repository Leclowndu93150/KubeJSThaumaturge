package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.parts.GolemAddon;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

public class GolemAddonBuilder extends GolemPartBuilder<GolemAddon> {
    public static final ResourceKey<Registry<GolemAddon>> REGISTRY = GolemAddon.REGISTRY_KEY;

    private final List<GolemPartModel> extraModels = new ArrayList<>();

    public GolemAddonBuilder(ResourceLocation id) {
        super(id, GolemPartModel.AttachPoint.BODY);
    }

    @Override
    protected String langKind() {
        return "addon";
    }

    public GolemAddonBuilder extraModel(ResourceLocation mesh, GolemPartModel.AttachPoint point) {
        return extraModel(mesh, null, point);
    }

    public GolemAddonBuilder extraModel(
            ResourceLocation mesh, @Nullable ResourceLocation texture, GolemPartModel.AttachPoint point) {
        extraModels.add(new GolemPartModel(mesh, texture, point));
        return this;
    }

    @Override
    public GolemAddon createObject() {
        List<GolemPartModel> models = new ArrayList<>(extraModels.size() + 1);
        GolemPartModel primary = partModel();
        if (primary != null) {
            models.add(primary);
        }
        models.addAll(extraModels);
        return GolemAddon.withModels(research, icon, models, components, onTick, resolvedTraits());
    }
}
