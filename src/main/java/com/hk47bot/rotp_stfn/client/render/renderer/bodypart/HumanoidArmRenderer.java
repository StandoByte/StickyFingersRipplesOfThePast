package com.hk47bot.rotp_stfn.client.render.renderer.bodypart;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.client.HumanoidParser;
import com.hk47bot.rotp_stfn.client.HumanoidUtil;
import com.hk47bot.rotp_stfn.client.render.model.bodypart.PlayerArmModel;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerArmEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class HumanoidArmRenderer extends SimpleEntityRenderer<PlayerArmEntity, PlayerArmModel> {
    public HumanoidArmRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PlayerArmModel(), DefaultPlayerSkin.getDefaultSkin());
    }

    @Override
    protected void doRender(PlayerArmEntity entity, PlayerArmModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        final Minecraft mc = Minecraft.getInstance();
        World world = ClientUtil.getClientWorld();
        ZipperWorldCap cap = world.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
        if (entity.getOwner() != null && cap.isHumanoid(entity.getOwner())){
            LivingRenderer renderer = (LivingRenderer) mc.getEntityRenderDispatcher().getRenderer(entity.getOwner());
            renderer.scale(entity.getOwner(), matrixStack, partialTick);
            EntityModel entityModel = renderer.getModel();
            EntityZipperCapability zipperCap = entity.getOwner().getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (zipperCap.isLeftArmBlocked() || zipperCap.isRightArmBlocked()){
                ModelRenderer rightArm = HumanoidParser.getPartByName("rightArm", entityModel);
                ModelRenderer leftArm = HumanoidParser.getPartByName("leftArm", entityModel);
                if (rightArm != null && entity.isRight()){
                    HumanoidUtil.renderPart(entity, rightArm, matrixStack, buffer, renderer.getTextureLocation(entity.getOwner()), packedLight, partialTick, true, -2, -2, 2);
                }
                else if (leftArm != null){
                    HumanoidUtil.renderPart(entity, leftArm, matrixStack, buffer, renderer.getTextureLocation(entity.getOwner()), packedLight, partialTick, true, -2, -2, 2);
                }
                Entity leashHolder = entity.getLeashHolder();
                if (leashHolder != null){
                    HumanoidUtil.renderLeash(entity, packedLight, matrixStack, buffer, leashHolder);
                }
            }
        }
        else {
            super.doRender(entity, model, partialTick, matrixStack, buffer, packedLight);
        }
    }
}
