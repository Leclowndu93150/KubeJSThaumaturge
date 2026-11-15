package com.leclowndu93150.kubejs_thaumaturge.recipe;

import com.leclowndu93150.thaumaturge.TTIds;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.component.TagKeyComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;
import java.util.List;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public interface ThaumaturgeRecipeSchemas {
    RecipeKey<ItemStackTemplate> RESULT = ThaumaturgeRecipeComponents.ITEM_STACK_TEMPLATE.outputKey("result");
    RecipeKey<Ingredient> CATALYST = IngredientComponent.INGREDIENT.inputKey("catalyst");
    RecipeKey<AspectList> ASPECTS = ThaumaturgeRecipeComponents.ASPECT_LIST.inputKey("aspects");
    RecipeKey<ResearchGate> RESEARCH =
            ThaumaturgeRecipeComponents.RESEARCH_GATE.otherKey("research").optional((ResearchGate) null);

    RecipeSchema CRUCIBLE = new RecipeSchema(RESULT, CATALYST, ASPECTS, RESEARCH).uniqueId(RESULT);

    RecipeKey<List<Ingredient>> COMPONENTS =
            IngredientComponent.INGREDIENT.asList().inputKey("components");
    RecipeKey<Integer> INSTABILITY =
            NumberComponent.intRange(0, 100).otherKey("instability").optional(0);

    RecipeSchema INFUSION =
            new RecipeSchema(RESULT, CATALYST, COMPONENTS, ASPECTS, INSTABILITY, RESEARCH).uniqueId(RESULT);

    RecipeKey<String> ENCHANTMENT = StringComponent.STRING.inputKey("enchantment");
    RecipeKey<Ingredient> DISPLAY_CATALYST = IngredientComponent.INGREDIENT.inputKey("display_catalyst");

    RecipeSchema INFUSION_ENCHANTMENT = new RecipeSchema(ENCHANTMENT, DISPLAY_CATALYST, COMPONENTS, ASPECTS, RESEARCH);

    RecipeKey<Integer> VIS = NumberComponent.intRange(1, Integer.MAX_VALUE).inputKey("vis");
    RecipeKey<AspectList> CRYSTALS = ThaumaturgeRecipeComponents.ASPECT_LIST.inputKey("crystals");
    RecipeKey<List<Ingredient>> INGREDIENTS =
            IngredientComponent.INGREDIENT.asList().inputKey("ingredients");
    RecipeKey<Boolean> SHOW_NOTIFICATION =
            BooleanComponent.BOOLEAN.otherKey("show_notification").optional(true);

    RecipeSchema ARCANE_SHAPELESS =
            new RecipeSchema(RESULT, INGREDIENTS, VIS, CRYSTALS, RESEARCH, SHOW_NOTIFICATION).uniqueId(RESULT);

    RecipeKey<List<String>> PATTERN = StringComponent.STRING.asList().otherKey("pattern");
    RecipeKey<TinyMap<Character, Ingredient>> KEY =
            IngredientComponent.OPTIONAL_INGREDIENT.asPatternKey().inputKey("key");

    RecipeSchema ARCANE_SHAPED =
            new RecipeSchema(RESULT, PATTERN, KEY, VIS, CRYSTALS, RESEARCH, SHOW_NOTIFICATION).uniqueId(RESULT);

    RecipeKey<DataComponentPatch> CATALYST_PATCH =
            ThaumaturgeRecipeComponents.COMPONENT_PATCH.outputKey("catalyst_patch");

    RecipeSchema INFUSION_PATCH = new RecipeSchema(CATALYST_PATCH, CATALYST, COMPONENTS, ASPECTS, INSTABILITY, RESEARCH)
            .typeOverride(TTIds.rl("infusion"));

    RecipeKey<Block> TARGET = BlockComponent.BLOCK.inputKey("target");
    RecipeKey<TagKey<Block>> TARGET_TAG = TagKeyComponent.BLOCK.inputKey("target_tag");
    RecipeKey<String> BLUEPRINT = StringComponent.ID.inputKey("blueprint");

    RecipeSchema DUST_TRIGGER_SIMPLE = new RecipeSchema(RESULT, TARGET, RESEARCH).uniqueId(RESULT);
    RecipeSchema DUST_TRIGGER_TAG = new RecipeSchema(RESULT, TARGET_TAG, RESEARCH).uniqueId(RESULT);
    RecipeSchema DUST_TRIGGER_MULTIBLOCK = new RecipeSchema(RESULT, BLUEPRINT, RESEARCH).uniqueId(RESULT);
}
