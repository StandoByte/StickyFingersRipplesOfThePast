package com.hk47bot.rotp_stfn.client.render.renderer.projectile;

import com.github.standobyte.jojo.client.render.entity.renderer.damaging.extending.ExtendingEntityRenderer;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.client.render.model.ownerbound.repeating.ExtendedPunchModel;
import com.hk47bot.rotp_stfn.entity.projectile.ExtendedPunchEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class ExtendedPunchRenderer extends ExtendingEntityRenderer<ExtendedPunchEntity, ExtendedPunchModel> {

    public ExtendedPunchRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ExtendedPunchModel(), new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/entity/projectiles/stfn_extended_punch.png"));
    }

}
