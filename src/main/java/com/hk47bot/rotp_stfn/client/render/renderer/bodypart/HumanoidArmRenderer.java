package com.hk47bot.rotp_stfn.client.render.renderer.bodypart;

import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.hk47bot.rotp_stfn.client.render.model.bodypart.PlayerArmModel;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerArmEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class HumanoidArmRenderer extends SimpleEntityRenderer<PlayerArmEntity, PlayerArmModel> {
    public HumanoidArmRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PlayerArmModel(), DefaultPlayerSkin.getDefaultSkin());
    }

    @Override
    protected void doRender(PlayerArmEntity entity, PlayerArmModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        final Minecraft mc = Minecraft.getInstance();
        if (entity.getOwner() != null){
            EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getOwner().getType();
            LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = (LivingRenderer<LivingEntity, EntityModel<LivingEntity>>) mc.getEntityRenderDispatcher().renderers.get(entityType);
            if (renderer != null){
                EntityModel entityModel = renderer.getModel();
                if (entityModel instanceof BipedModel){
                    BipedModel humanoidModel = (BipedModel) entityModel;
                    HumanoidUtil.renderPart(entity, entity.isRight() ? humanoidModel.rightArm : humanoidModel.leftArm, matrixStack, buffer, renderer.getTextureLocation(entity.getOwner()), packedLight, true, -2, -2, 2);
                    Entity leashHolder = entity.getLeashHolder();
                    if (leashHolder != null){
                        HumanoidUtil.renderLeash(entity, packedLight, matrixStack, buffer, leashHolder);
                    }
                }
            }
        }
    }


//    @Override
//    public ResourceLocation getTextureLocation(PlayerArmEntity entity) {
//        Entity owner = entity.getOwner();
//        return owner != null ? entityRenderDispatcher.getRenderer(owner).getTextureLocation(owner) :
//                Minecraft.getInstance().player.getSkinTextureLocation();
//    }
}
