package com.hk47bot.rotp_stfn.client.render.renderer.bodypart;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.render.entity.renderer.SimpleEntityRenderer;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.client.HumanoidParser;
import com.hk47bot.rotp_stfn.client.HumanoidUtil;
import com.hk47bot.rotp_stfn.client.render.model.bodypart.PlayerHeadModel;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class HumanoidHeadRenderer extends SimpleEntityRenderer<PlayerHeadEntity, PlayerHeadModel> {
    public HumanoidHeadRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PlayerHeadModel(), DefaultPlayerSkin.getDefaultSkin());
    }

    @Override
    protected void doRender(PlayerHeadEntity entity, PlayerHeadModel model, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        final Minecraft mc = Minecraft.getInstance();
        if (entity.getOwner() != null){
            LivingRenderer renderer = (LivingRenderer) mc.getEntityRenderDispatcher().getRenderer(entity.getOwner());
            EntityModel entityModel = renderer.getModel();
            World world = ClientUtil.getClientWorld();
            ZipperWorldCap cap = world.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
            EntityZipperCapability zipperCap = entity.getOwner().getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (cap.isHumanoid(entity.getOwner()) && !zipperCap.isHasHead()){
                HumanoidUtil.renderPart(entity, zipperCap, HumanoidParser.getPartByName("head", entityModel), matrixStack, buffer, renderer.getTextureLocation(entity.getOwner()), packedLight, partialTick, false, 0, 0, 0);
                Entity leashHolder = entity.getLeashHolder();
                if (leashHolder != null){
                    HumanoidUtil.renderLeash(entity, partialTick, matrixStack, buffer, leashHolder);
                }
            }
        }
    }
}
