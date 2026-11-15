package com.leclowndu93150.kubejs_thaumaturge.builder;

import com.leclowndu93150.thaumaturge.api.client.golems.GolemAccessoryAnchor;
import com.leclowndu93150.thaumaturge.api.golems.accessory.GolemAccessories;
import com.leclowndu93150.thaumaturge.api.golems.accessory.GolemAccessory;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public class GolemAccessoryItemBuilder extends ItemBuilder {
    public transient Identifier accessoryId;
    public transient Identifier group = GolemAccessory.Group.NONE.id();
    public transient int healthBonus;
    public transient float rangeFactor = 1.0F;
    public transient float speedFactor = 1.0F;
    public transient float regenFactor = 1.0F;
    public transient int armorBonus;
    public transient boolean killCredit;
    public transient @Nullable GolemAccessoryAnchor renderAnchor = GolemAccessoryAnchor.HEAD;
    public transient float renderX;
    public transient float renderY = 0.5F;
    public transient float renderZ;
    public transient float renderScale = 0.42F;
    public transient float renderRotation;
    public transient @Nullable GolemAccessory accessory;

    public GolemAccessoryItemBuilder(Identifier id) {
        super(id);
        this.accessoryId = id;
        unstackable();
    }

    public GolemAccessoryItemBuilder accessoryId(Identifier id) {
        this.accessoryId = id;
        return this;
    }

    public GolemAccessoryItemBuilder group(Identifier group) {
        this.group = group;
        return this;
    }

    public GolemAccessoryItemBuilder health(int bonus) {
        this.healthBonus = bonus;
        return this;
    }

    public GolemAccessoryItemBuilder armor(int bonus) {
        this.armorBonus = bonus;
        return this;
    }

    public GolemAccessoryItemBuilder rangeFactor(float factor) {
        this.rangeFactor = factor;
        return this;
    }

    public GolemAccessoryItemBuilder speedFactor(float factor) {
        this.speedFactor = factor;
        return this;
    }

    public GolemAccessoryItemBuilder regenFactor(float factor) {
        this.regenFactor = factor;
        return this;
    }

    public GolemAccessoryItemBuilder killCredit(boolean killCredit) {
        this.killCredit = killCredit;
        return this;
    }

    public GolemAccessoryItemBuilder renderOn(GolemAccessoryAnchor anchor) {
        this.renderAnchor = anchor;
        return this;
    }

    public GolemAccessoryItemBuilder noRender() {
        this.renderAnchor = null;
        return this;
    }

    public GolemAccessoryItemBuilder renderOffset(float x, float y, float z) {
        this.renderX = x;
        this.renderY = y;
        this.renderZ = z;
        return this;
    }

    public GolemAccessoryItemBuilder renderScale(float scale) {
        this.renderScale = scale;
        return this;
    }

    public GolemAccessoryItemBuilder renderRotation(float degrees) {
        this.renderRotation = degrees;
        return this;
    }

    @Override
    public Item createObject() {
        accessory = GolemAccessories.register(new GolemAccessory(
                accessoryId,
                new GolemAccessory.Group(group),
                healthBonus,
                rangeFactor,
                speedFactor,
                regenFactor,
                armorBonus,
                killCredit));
        return super.createObject();
    }
}
