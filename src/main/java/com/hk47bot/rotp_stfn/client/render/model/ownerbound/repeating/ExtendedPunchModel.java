package com.hk47bot.rotp_stfn.client.render.model.ownerbound.repeating;

import com.github.standobyte.jojo.client.render.entity.model.ownerbound.repeating.RepeatingModel;
import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.OwnerBoundProjectileEntity;

import com.hk47bot.rotp_stfn.entity.projectile.ExtendedPunchEntity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class ExtendedPunchModel extends RepeatingModel<ExtendedPunchEntity> {

    private final ModelRenderer zipper;
    private final ModelRenderer rightForeArm;
    private final ModelRenderer zipper2;

    public ExtendedPunchModel() {
        texWidth = 64;
        texHeight = 64;

        zipper = new ModelRenderer(this);
        zipper.setPos(0.0F, 1.0F, 0.0F);

        zipper.texOffs(17, 0).addBox(-0.5F, -1.9F, -8.0F, 1.0F, 0.0F, 16.0F, 0.0F, false);

        rightForeArm = new ModelRenderer(this);
        rightForeArm.setPos(0.0F, 0.1F, -6.8333F);
        rightForeArm.texOffs(0, 0).addBox(-2.0F, -0.5F, -1.1667F, 4.0F, 4.0F, 6.0F, 0.0F, false);
        rightForeArm.texOffs(0, 10).addBox(-2.0F, -1.0F, -1.1667F, 4.0F, 1.0F, 6.0F, -0.1F, false);
        rightForeArm.texOffs(15, 13).addBox(1.0F, -0.75F, -1.4167F, 1.0F, 1.0F, 1.0F, -0.11F, false);
        rightForeArm.texOffs(15, 13).addBox(0.0F, -0.75F, -1.4167F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        rightForeArm.texOffs(15, 13).addBox(-1.0F, -0.75F, -1.4167F, 1.0F, 1.0F, 1.0F, -0.1F, false);
        rightForeArm.texOffs(15, 13).addBox(-2.0F, -0.75F, -1.4167F, 1.0F, 1.0F, 1.0F, -0.11F, false);

        zipper2 = new ModelRenderer(this);
        zipper2.setPos(0.0F, -1.0F, 4.0833F);
        rightForeArm.addChild(zipper2);
        setRotationAngle(zipper2, -0.0873F, 0.0F, 0.0F);
        zipper2.texOffs(23, 0).addBox(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, -0.08F, false);
        zipper2.texOffs(23, 2).addBox(-0.5F, -0.5F, -2.25F, 1.0F, 1.0F, 2.0F, -0.21F, false);
        zipper2.texOffs(23, 5).addBox(-1.0F, -0.5F, -3.5F, 2.0F, 1.0F, 2.0F, -0.2F, false);
        zipper2.texOffs(23, 8).addBox(-1.0F, -0.5F, -4.75F, 2.0F, 1.0F, 2.0F, -0.1F, false);
    }

    @Override
    protected ModelRenderer getMainPart() {
        return rightForeArm;
    }

    @Override
    protected float getMainPartLength() {
        return 2F;
    }

    @Override
    protected ModelRenderer getRepeatingPart() {
        return zipper;
    }

    @Override
    protected float getRepeatingPartLength() {
        return 8F;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
