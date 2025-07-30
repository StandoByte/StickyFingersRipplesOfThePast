package com.hk47bot.rotp_stfn.client.render.renderer;

import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.hk47bot.rotp_stfn.client.render.model.PlayerHeadModel;
import com.hk47bot.rotp_stfn.entity.PlayerHeadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class PlayerHeadRenderer extends SimpleEntityRenderer<PlayerHeadEntity, PlayerHeadModel> {
    public PlayerHeadRenderer(EntityRendererManager renderManager, PlayerHeadModel model, ResourceLocation texPath) {
        super(renderManager, new PlayerHeadModel(), DefaultPlayerSkin.getDefaultSkin());
    }

    @Override
    protected void doRender(PlayerHeadEntity entity, PlayerHeadModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        renderModel(entity, model, partialTick, matrixStack, buffer.getBuffer(RenderType.entityTranslucentCull(getTextureLocation(entity))), packedLight);
    }

    public ResourceLocation getTextureLocation(PlayerHeadEntity entity) {
        Entity owner = entity.getOwner();
        return owner != null ? entityRenderDispatcher.getRenderer(owner).getTextureLocation(owner) :
                Minecraft.getInstance().player.getSkinTextureLocation();
    }
}
