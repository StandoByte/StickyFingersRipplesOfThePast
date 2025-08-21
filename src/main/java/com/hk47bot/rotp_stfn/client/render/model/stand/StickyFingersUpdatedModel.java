package com.hk47bot.rotp_stfn.client.render.model.stand;

import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;
import net.minecraft.client.renderer.model.ModelRenderer;

public class StickyFingersUpdatedModel extends HumanoidStandModel<StickyFingersEntity> {
    private ModelRenderer bone;
    private ModelRenderer bone2;
    private ModelRenderer bone3;

    public StickyFingersUpdatedModel() {
        super();
    }

    @Override
    public void prepareMobModel(StickyFingersEntity entity, float walkAnimPos, float walkAnimSpeed, float partialTick) {
        if (entity != null){
            super.prepareMobModel(entity, walkAnimPos, walkAnimSpeed, partialTick);
            if (rightForeArm != null && rightArmJoint != null) {
                rightForeArm.visible = entity.hasForeArm();
                rightArmJoint.visible = entity.hasShortForeArm();
            }
        }
    }
}
