package com.leclowndu93150.kubejs_thaumaturge.event;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftCost;
import com.leclowndu93150.thaumaturge.api.recipe.ArcaneCraftCostEvent;
import com.leclowndu93150.thaumaturge.api.recipe.IArcaneRecipe;
import dev.latvian.mods.kubejs.player.KubePlayerEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

public class ArcaneCraftCostKubeEvent implements KubePlayerEvent {
    private final ArcaneCraftCostEvent event;

    public ArcaneCraftCostKubeEvent(ArcaneCraftCostEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.getPlayer();
    }

    public IArcaneRecipe getRecipe() {
        return event.getRecipe();
    }

    public boolean isPaidFromWand() {
        return event.getCost().paidFromWand();
    }

    public Map<Identifier, Integer> getWandCentivis() {
        Map<Identifier, Integer> centivis = new LinkedHashMap<>();
        event.getCost().wandCentivis().forEach((key, amount) -> centivis.put(key.identifier(), amount));
        return centivis;
    }

    public void setWandCentivis(Map<Identifier, Integer> centivis) {
        Map<ResourceKey<IAspect>, Integer> keyed = new LinkedHashMap<>();
        centivis.forEach((id, amount) -> keyed.put(ResourceKey.create(IAspect.REGISTRY_KEY, id), amount));
        ArcaneCraftCost cost = event.getCost();
        event.setCost(new ArcaneCraftCost(
                cost.paidFromWand(), keyed, cost.crystalsNeeded(), cost.auraVis(), cost.affordable()));
    }

    public AspectList getCrystals() {
        return event.getCost().crystalsNeeded();
    }

    public void setCrystals(AspectList crystals) {
        ArcaneCraftCost cost = event.getCost();
        event.setCost(new ArcaneCraftCost(
                cost.paidFromWand(),
                cost.wandCentivis(),
                crystals == null ? AspectList.EMPTY : crystals,
                cost.auraVis(),
                cost.affordable()));
    }

    public int getAuraVis() {
        return event.getCost().auraVis();
    }

    public void setAuraVis(int vis) {
        ArcaneCraftCost cost = event.getCost();
        event.setCost(new ArcaneCraftCost(
                cost.paidFromWand(), cost.wandCentivis(), cost.crystalsNeeded(), Math.max(0, vis), cost.affordable()));
    }

    public boolean isAffordable() {
        return event.getCost().affordable();
    }

    public void setAffordable(boolean affordable) {
        ArcaneCraftCost cost = event.getCost();
        event.setCost(new ArcaneCraftCost(
                cost.paidFromWand(), cost.wandCentivis(), cost.crystalsNeeded(), cost.auraVis(), affordable));
    }

    public void free() {
        event.setCost(new ArcaneCraftCost(event.getCost().paidFromWand(), Map.of(), AspectList.EMPTY, 0, true));
    }
}
