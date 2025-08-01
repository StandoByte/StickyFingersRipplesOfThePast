package com.hk47bot.rotp_stfn.client.render.renderer.bodypart;

import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.hk47bot.rotp_stfn.client.render.model.bodypart.PlayerArmModel;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerArmEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class PlayerArmRenderer extends SimpleEntityRenderer<PlayerArmEntity, PlayerArmModel> {
    public PlayerArmRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PlayerArmModel(), DefaultPlayerSkin.getDefaultSkin());
    }

    @Override
    protected void doRender(PlayerArmEntity entity, PlayerArmModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        renderModel(entity, model, partialTick, matrixStack, buffer.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity))), packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PlayerArmEntity entity) {
        Entity owner = entity.getOwner();
        return owner != null ? entityRenderDispatcher.getRenderer(owner).getTextureLocation(owner) :
                Minecraft.getInstance().player.getSkinTextureLocation();
    }
}
