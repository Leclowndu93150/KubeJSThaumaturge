package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.golems.GolemTrait;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemComponent;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPart;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import com.leclowndu93150.thaumaturge.api.golems.parts.IGolemPartAbility;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class GolemPartBuilder<T> extends BuilderBase<T> {
    protected final List<ResourceLocation> research = new ArrayList<>();
    protected ResourceLocation icon;
    protected final List<ResourceLocation> traits = new ArrayList<>();
    protected final List<GolemComponent> components = new ArrayList<>();
    protected @Nullable ResourceLocation objModel;
    protected @Nullable ResourceLocation modelTexture;
    protected GolemPartModel.AttachPoint attachPoint;
    protected @Nullable IGolemPartAbility onTick;
    protected String partName;
    protected String partText;

    protected GolemPartBuilder(ResourceLocation id, GolemPartModel.AttachPoint attachPoint) {
        super(id);
        this.attachPoint = attachPoint;
        this.icon = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(), "textures/misc/golem/" + id.getPath() + ".png");
    }

    protected abstract String langKind();

    public GolemPartBuilder<T> partName(String name) {
        this.partName = name;
        return this;
    }

    public GolemPartBuilder<T> partText(String text) {
        this.partText = text;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);
        String base = partName != null
                ? partName
                : Character.toUpperCase(id.getPath().charAt(0)) + id.getPath().substring(1);
        lang.add(id.getNamespace(), GolemPart.nameKey(langKind(), id), base);
        lang.add(id.getNamespace(), GolemPart.descriptionKey(langKind(), id), partText != null ? partText : base);
    }

    public GolemPartBuilder<T> research(List<ResourceLocation> entries) {
        this.research.clear();
        this.research.addAll(entries);
        return this;
    }

    public GolemPartBuilder<T> icon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public GolemPartBuilder<T> traits(List<ResourceLocation> traits) {
        this.traits.clear();
        this.traits.addAll(traits);
        return this;
    }

    public GolemPartBuilder<T> model(ResourceLocation objModel) {
        this.objModel = objModel;
        return this;
    }

    public GolemPartBuilder<T> modelTexture(ResourceLocation texture) {
        this.modelTexture = texture;
        return this;
    }

    public GolemPartBuilder<T> attachPoint(GolemPartModel.AttachPoint point) {
        this.attachPoint = point;
        return this;
    }

    public GolemPartBuilder<T> component(ResourceLocation item) {
        this.components.add(GolemComponent.of(() -> new ItemStack(BuiltInRegistries.ITEM.get(item))));
        return this;
    }

    public GolemPartBuilder<T> materialBase() {
        this.components.add(GolemComponent.base());
        return this;
    }

    public GolemPartBuilder<T> materialMechanism() {
        this.components.add(GolemComponent.mechanism());
        return this;
    }

    public GolemPartBuilder<T> onTick(IGolemPartAbility callback) {
        this.onTick = callback;
        return this;
    }

    protected @Nullable GolemPartModel partModel() {
        return objModel == null ? null : new GolemPartModel(objModel, modelTexture, attachPoint);
    }

    protected List<Holder<GolemTrait>> resolvedTraits() {
        List<Holder<GolemTrait>> resolved = new ArrayList<>(traits.size());
        for (ResourceLocation trait : traits) {
            resolved.add(GolemTraits.holder(trait));
        }
        return resolved;
    }
}
