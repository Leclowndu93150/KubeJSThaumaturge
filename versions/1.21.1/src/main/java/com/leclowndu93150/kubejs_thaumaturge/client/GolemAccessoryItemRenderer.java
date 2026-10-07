package com.leclowndu93150.kubejs_thaumaturge.client;

import com.leclowndu93150.kubejs_thaumaturge.builder.GolemAccessoryItemBuilder;
import com.leclowndu93150.thaumaturge.api.client.golems.GolemAccessoryRenderContext;
import com.leclowndu93150.thaumaturge.api.client.golems.GolemAccessoryRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class GolemAccessoryItemRenderer implements GolemAccessoryRenderer {
    private final GolemAccessoryItemBuilder builder;

    public GolemAccessoryItemRenderer(GolemAccessoryItemBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffers, GolemAccessoryRenderContext context) {
        poseStack.pushPose();
        poseStack.translate(builder.renderX, builder.renderY, builder.renderZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + builder.renderRotation));
        poseStack.scale(builder.renderScale, builder.renderScale, builder.renderScale);
        Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(
                        new ItemStack(builder.get()),
                        ItemDisplayContext.HEAD,
                        context.lightCoords(),
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        buffers,
                        null,
                        0);
        poseStack.popPose();
    }
}
