package com.hk47bot.rotp_stfn.client.render.renderer.stand;

import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.client.render.model.stand.StickyFingersModel;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class StickyFingersRenderer extends StandEntityRenderer<StickyFingersEntity, StickyFingersModel> {
    public StickyFingersRenderer(EntityRendererManager renderManager) {

        super(renderManager, new StickyFingersModel(), new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/entity/stand/sticky_fingers.png"), 0);

    }
    private static final ResourceLocation ANOTHER_STAND_TEXTURE = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/entity/stand/sticky_fingers2.png");
    @Override
    public ResourceLocation getTextureLocation(StickyFingersEntity entity) {
        boolean useTexture2 = entity.isUsingExtendedPunch();
        ResourceLocation standTexture;
        if (useTexture2) {
            standTexture = ANOTHER_STAND_TEXTURE;
        }
        else {
            standTexture = super.getTextureLocation(entity);
        }
        return standTexture;
    }
}
