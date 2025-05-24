package com.hk47bot.rotp_stfn.client.render.renderer.stand;

import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.client.render.model.stand.StickyFingersUpdatedModel;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class StickyFingersUpdatedRenderer extends StandEntityRenderer<StickyFingersEntity, StandEntityModel<StickyFingersEntity>> {

    public StickyFingersUpdatedRenderer(EntityRendererManager renderManager) {
        super(renderManager,
                StandModelRegistry.registerModel(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers"), StickyFingersUpdatedModel::new),
                new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/entity/stand/sticky_fingers_new.png"), 0);
    }
    @Override
    public ResourceLocation getTextureLocation(StickyFingersEntity entity) {
        boolean useTexture2 = entity.isUsingExtendedPunch();
        ResourceLocation standTexture;
        if (useTexture2) {
            standTexture = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/entity/stand/sticky_fingers_ranged_attack.png");
        }
        else {
            standTexture = super.getTextureLocation(entity);
        }
        return standTexture;
    }
}
