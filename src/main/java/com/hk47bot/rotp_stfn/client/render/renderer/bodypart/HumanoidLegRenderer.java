package com.hk47bot.rotp_stfn.client.render.renderer.bodypart;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.client.HumanoidParser;
import com.hk47bot.rotp_stfn.client.HumanoidUtil;
import com.hk47bot.rotp_stfn.client.render.model.bodypart.PlayerLegModel;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedLegEntity;
import com.hk47bot.rotp_stfn.mixin.client.LivingRendererInvoker;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class HumanoidLegRenderer extends SimpleEntityRenderer<UnzippedLegEntity, PlayerLegModel> {
    public HumanoidLegRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PlayerLegModel(), DefaultPlayerSkin.getDefaultSkin());
    }

    @Override
    protected void doRender(UnzippedLegEntity entity, PlayerLegModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        final Minecraft mc = Minecraft.getInstance();
        World world = ClientUtil.getClientWorld();
        ZipperWorldCap cap = world.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
        if (entity.getOwner() != null && cap.isHumanoid(entity.getOwner())){
            LivingRenderer renderer = (LivingRenderer) mc.getEntityRenderDispatcher().getRenderer(entity.getOwner());
            ((LivingRendererInvoker) renderer).invokeScale(entity.getOwner(), matrixStack, partialTick);
            EntityModel entityModel = renderer.getModel();
            EntityZipperCapability zipperCap = entity.getOwner().getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (zipperCap.isLeftLegBlocked() || zipperCap.isRightLegBlocked()){
                ModelRenderer rightLeg = HumanoidParser.getPartByName("rightLeg", entityModel);
                ModelRenderer leftLeg = HumanoidParser.getPartByName("leftLeg", entityModel);
                if (rightLeg != null && entity.isRight()){
                    HumanoidUtil.renderPart(entity, rightLeg, matrixStack, buffer, renderer.getTextureLocation(entity.getOwner()), packedLight, partialTick, true, -2, -2, 2);
                }
                else if (leftLeg != null){
                    HumanoidUtil.renderPart(entity, leftLeg, matrixStack, buffer, renderer.getTextureLocation(entity.getOwner()), packedLight, partialTick, true, -2, -2, 2);
                }
                Entity leashHolder = entity.getLeashHolder();
                if (leashHolder != null){
                    HumanoidUtil.renderLeash(entity, packedLight, matrixStack, buffer, leashHolder);
                }
            }
        }
    }
}

