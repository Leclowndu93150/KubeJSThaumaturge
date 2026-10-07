package com.leclowndu93150.kubejs_thaumaturge.client;

import com.leclowndu93150.kubejs_thaumaturge.builder.GolemAccessoryItemBuilder;
import com.leclowndu93150.thaumaturge.api.client.golems.GolemAccessoryRenderContext;
import com.leclowndu93150.thaumaturge.api.client.golems.GolemAccessoryRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class GolemAccessoryItemRenderer implements GolemAccessoryRenderer {
    private final GolemAccessoryItemBuilder builder;

    public GolemAccessoryItemRenderer(GolemAccessoryItemBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, GolemAccessoryRenderContext context) {
        ItemStackRenderState state = new ItemStackRenderState();
        Minecraft.getInstance()
                .getItemModelResolver()
                .updateForTopItem(state, new ItemStack(builder.get()), ItemDisplayContext.HEAD, null, null, 0);
        poseStack.pushPose();
        poseStack.translate(builder.renderX, builder.renderY, builder.renderZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + builder.renderRotation));
        poseStack.scale(builder.renderScale, builder.renderScale, builder.renderScale);
        state.submit(poseStack, collector, context.lightCoords(), OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
