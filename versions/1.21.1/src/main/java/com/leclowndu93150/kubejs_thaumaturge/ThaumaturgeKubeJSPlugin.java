package com.leclowndu93150.kubejs_thaumaturge;

import com.google.gson.JsonObject;
import com.leclowndu93150.kubejs_thaumaturge.binding.AspectPoolsWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.DataMapsWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.EssentiaWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.GolemsWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.MobTraitsWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.ResearchRequirements;
import com.leclowndu93150.kubejs_thaumaturge.binding.ResearchWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.SpellsWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.VisRelayWrapper;
import com.leclowndu93150.kubejs_thaumaturge.binding.WandVisWrapper;
import com.leclowndu93150.kubejs_thaumaturge.builder.CodecRegistryBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemAccessoryItemBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemAddonBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemArmBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemHeadBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemLegBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemMaterialBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.GolemTraitBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.JarBlockBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.MobTraitBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.StabiliserBlockBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.WandCapBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.WandCapItemBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.WandRodBuilder;
import com.leclowndu93150.kubejs_thaumaturge.builder.WandRodItemBuilder;
import com.leclowndu93150.kubejs_thaumaturge.event.ThaumaturgeEvents;
import com.leclowndu93150.kubejs_thaumaturge.recipe.ThaumaturgeRecipeComponents;
import com.leclowndu93150.kubejs_thaumaturge.recipe.ThaumaturgeRecipeSchemas;
import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectDataMaps;
import com.leclowndu93150.thaumaturge.api.aspect.AspectIndexAccess;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeAccess;
import com.leclowndu93150.thaumaturge.api.items.GogglesAccess;
import com.leclowndu93150.thaumaturge.api.items.RechargeAccess;
import com.leclowndu93150.thaumaturge.api.labyrinth.LabyrinthEncounter;
import com.leclowndu93150.thaumaturge.api.recipe.Blueprint;
import com.leclowndu93150.thaumaturge.api.research.IResearchCategory;
import com.leclowndu93150.thaumaturge.api.research.IResearchEntry;
import com.leclowndu93150.thaumaturge.api.research.scan.ScanEntry;
import com.leclowndu93150.thaumaturge.api.research.scan.ScanTarget;
import com.leclowndu93150.thaumaturge.api.research.scan.ScanningManager;
import com.leclowndu93150.thaumaturge.api.spell.affinity.AspectAffinity;
import com.leclowndu93150.thaumaturge.api.spell.part.SpellPart;
import com.leclowndu93150.thaumaturge.api.taint.TaintApi;
import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;
import com.leclowndu93150.thaumaturge.content.eldritch.labyrinth.definition.LabyrinthDefinition;
import com.leclowndu93150.thaumaturge.content.eldritch.labyrinth.definition.RoomType;
import com.leclowndu93150.thaumaturge.content.eldritch.site.ObeliskSite;
import com.leclowndu93150.thaumaturge.content.infusion.instability.InstabilityOutcome;
import com.leclowndu93150.thaumaturge.content.pech.PechTradeTable;
import com.leclowndu93150.thaumaturge.content.research.ResearchCategory;
import com.leclowndu93150.thaumaturge.content.research.ResearchEntry;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.RegistryObjectStorage;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class ThaumaturgeKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.addDefault(WandCapBuilder.REGISTRY, WandCapBuilder.class, WandCapBuilder::new);
        registry.addDefault(WandRodBuilder.REGISTRY, WandRodBuilder.class, WandRodBuilder::new);
        registry.addDefault(GolemTraitBuilder.REGISTRY, GolemTraitBuilder.class, GolemTraitBuilder::new);
        registry.addDefault(GolemMaterialBuilder.REGISTRY, GolemMaterialBuilder.class, GolemMaterialBuilder::new);
        registry.addDefault(GolemHeadBuilder.REGISTRY, GolemHeadBuilder.class, GolemHeadBuilder::new);
        registry.addDefault(GolemArmBuilder.REGISTRY, GolemArmBuilder.class, GolemArmBuilder::new);
        registry.addDefault(GolemLegBuilder.REGISTRY, GolemLegBuilder.class, GolemLegBuilder::new);
        registry.addDefault(GolemAddonBuilder.REGISTRY, GolemAddonBuilder.class, GolemAddonBuilder::new);
        registry.addDefault(MobTraitBuilder.REGISTRY, MobTraitBuilder.class, MobTraitBuilder::new);
        codecDefault(registry, IAspect.REGISTRY_KEY, Aspect.CODEC);
        codecDefault(registry, IResearchCategory.REGISTRY_KEY, ResearchCategory.CODEC);
        codecDefault(registry, IResearchEntry.REGISTRY_KEY, ResearchEntry.CODEC);
        codecDefault(registry, ScanEntry.REGISTRY_KEY, ScanEntry.CODEC);
        codecDefault(registry, Blueprint.REGISTRY_KEY, Blueprint.CODEC);
        codecDefault(registry, PechTradeTable.REGISTRY_KEY, PechTradeTable.CODEC);
        codecDefault(registry, SpellPart.REGISTRY_KEY, SpellPart.CODEC);
        codecDefault(registry, AspectAffinity.REGISTRY_KEY, AspectAffinity.CODEC);
        codecDefault(registry, LabyrinthEncounter.REGISTRY_KEY, LabyrinthEncounter.DIRECT_CODEC);
        codecDefault(registry, RoomType.REGISTRY_KEY, RoomType.CODEC);
        codecDefault(registry, LabyrinthDefinition.REGISTRY_KEY, LabyrinthDefinition.CODEC);
        codecDefault(registry, ObeliskSite.REGISTRY_KEY, ObeliskSite.CODEC);
        codecDefault(registry, InstabilityOutcome.REGISTRY_KEY, InstabilityOutcome.CODEC);
        registry.of(Registries.BLOCK, reg -> {
            reg.add(
                    ResourceLocation.fromNamespaceAndPath("thaumaturge", "jar"),
                    JarBlockBuilder.class,
                    JarBlockBuilder::new);
            reg.add(
                    ResourceLocation.fromNamespaceAndPath("thaumaturge", "infusion_stabiliser"),
                    StabiliserBlockBuilder.class,
                    StabiliserBlockBuilder::new);
        });
        registry.of(Registries.ITEM, reg -> {
            reg.add(
                    ResourceLocation.fromNamespaceAndPath("thaumaturge", "wand_cap"),
                    WandCapItemBuilder.class,
                    WandCapItemBuilder::new);
            reg.add(
                    ResourceLocation.fromNamespaceAndPath("thaumaturge", "wand_rod"),
                    WandRodItemBuilder.class,
                    WandRodItemBuilder::new);
            reg.add(
                    ResourceLocation.fromNamespaceAndPath("thaumaturge", "golem_accessory"),
                    GolemAccessoryItemBuilder.class,
                    GolemAccessoryItemBuilder::new);
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> void codecDefault(
            BuilderTypeRegistry registry, ResourceKey<Registry<T>> key, Codec<? extends T> codec) {
        registry.addDefault(key, (Class) CodecRegistryBuilder.class, id -> new CodecRegistryBuilder<>(id, codec));
    }

    @Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
        registry.register(IAspect.REGISTRY_KEY, Aspect.CODEC, IAspect.class);
        registry.register(IResearchCategory.REGISTRY_KEY, ResearchCategory.CODEC, IResearchCategory.class);
        registry.register(IResearchEntry.REGISTRY_KEY, ResearchEntry.CODEC, IResearchEntry.class);
        registry.register(ScanEntry.REGISTRY_KEY, ScanEntry.CODEC, ScanEntry.class);
        registry.register(Blueprint.REGISTRY_KEY, Blueprint.CODEC, Blueprint.class);
        registry.register(PechTradeTable.REGISTRY_KEY, PechTradeTable.CODEC, PechTradeTable.class);
        registry.register(SpellPart.REGISTRY_KEY, SpellPart.CODEC, SpellPart.class);
        registry.register(AspectAffinity.REGISTRY_KEY, AspectAffinity.CODEC, AspectAffinity.class);
        registry.register(LabyrinthEncounter.REGISTRY_KEY, LabyrinthEncounter.DIRECT_CODEC, LabyrinthEncounter.class);
        registry.register(RoomType.REGISTRY_KEY, RoomType.CODEC, RoomType.class);
        registry.register(LabyrinthDefinition.REGISTRY_KEY, LabyrinthDefinition.CODEC, LabyrinthDefinition.class);
        registry.register(ObeliskSite.REGISTRY_KEY, ObeliskSite.CODEC, ObeliskSite.class);
        registry.register(InstabilityOutcome.REGISTRY_KEY, InstabilityOutcome.CODEC, InstabilityOutcome.class);
    }

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.register(ThaumaturgeRecipeComponents.ASPECT_LIST);
        registry.register(ThaumaturgeRecipeComponents.RESEARCH_GATE);
        registry.register(ThaumaturgeRecipeComponents.COMPONENT_PATCH);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(TTIds.rl("crucible"), ThaumaturgeRecipeSchemas.CRUCIBLE);
        registry.register(TTIds.rl("infusion"), ThaumaturgeRecipeSchemas.INFUSION);
        registry.register(TTIds.rl("infusion_enchantment"), ThaumaturgeRecipeSchemas.INFUSION_ENCHANTMENT);
        registry.register(TTIds.rl("arcane_workbench_shapeless"), ThaumaturgeRecipeSchemas.ARCANE_SHAPELESS);
        registry.register(TTIds.rl("arcane_workbench_shaped"), ThaumaturgeRecipeSchemas.ARCANE_SHAPED);
        registry.register(TTIds.rl("infusion_patch"), ThaumaturgeRecipeSchemas.INFUSION_PATCH);
        registry.register(TTIds.rl("dust_trigger_simple"), ThaumaturgeRecipeSchemas.DUST_TRIGGER_SIMPLE);
        registry.register(TTIds.rl("dust_trigger_tag"), ThaumaturgeRecipeSchemas.DUST_TRIGGER_TAG);
        registry.register(TTIds.rl("dust_trigger_multiblock"), ThaumaturgeRecipeSchemas.DUST_TRIGGER_MULTIBLOCK);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.registerCodec(AspectList.class, AspectList.CODEC);
        registry.register(ScanTarget.class, (TypeWrapperRegistry.ContextFromFunction<ScanTarget>) (cx, o) -> {
            if (o instanceof ScanTarget target) {
                return target;
            }
            if (o instanceof Entity entity) {
                return ScanTarget.entity(entity);
            }
            if (o instanceof BlockPos pos) {
                return ScanTarget.block(pos);
            }
            if (o instanceof ItemStack stack) {
                return ScanTarget.stack(stack);
            }
            return ScanTarget.stack((ItemStack) cx.jsToJava(o, TypeInfo.of(ItemStack.class)));
        });
    }

    @Override
    public void beforeScriptsLoaded(ScriptManager manager) {
        ResearchRequirements.clear(manager.scriptType);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(ThaumaturgeEvents.GROUP);
    }

    @Override
    @SuppressWarnings("removal")
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("Aura", AuraHelper.class);
        bindings.add("Warp", WarpHelper.class);
        bindings.add("Taint", TaintApi.class);
        bindings.add("Aspects", AspectIndexAccess.class);
        bindings.add("AspectPools", AspectPoolsWrapper.class);
        bindings.add("AspectDataMaps", AspectDataMaps.class);
        bindings.add("ThaumaturgeData", DataMapsWrapper.class);
        bindings.add("Research", ResearchWrapper.class);
        bindings.add("Knowledge", KnowledgeAccess.class);
        bindings.add("WandVis", WandVisWrapper.class);
        bindings.add("Essentia", EssentiaWrapper.class);
        bindings.add("VisRelay", VisRelayWrapper.class);
        bindings.add("Recharge", RechargeAccess.class);
        bindings.add("Goggles", GogglesAccess.class);
        bindings.add("Scanning", ScanningManager.class);
        bindings.add("Golems", GolemsWrapper.class);
        bindings.add("Spells", SpellsWrapper.class);
        bindings.add("MobTraits", MobTraitsWrapper.class);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        JsonObject values = new JsonObject();
        for (BuilderBase<?> builder : RegistryObjectStorage.ALL_BUILDERS) {
            if (builder instanceof GolemAccessoryItemBuilder accessory) {
                JsonObject entry = new JsonObject();
                entry.addProperty("accessory", accessory.accessoryId.toString());
                values.add(accessory.id.toString(), entry);
            }
        }
        if (values.isEmpty()) {
            return;
        }
        JsonObject file = new JsonObject();
        file.add("values", values);
        generator.json(ResourceLocation.fromNamespaceAndPath("thaumaturge", "data_maps/item/golem_accessory"), file);
    }
}
