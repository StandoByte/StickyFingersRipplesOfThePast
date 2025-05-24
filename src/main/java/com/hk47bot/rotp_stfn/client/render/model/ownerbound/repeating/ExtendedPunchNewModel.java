package com.hk47bot.rotp_stfn.client.render.model.ownerbound.repeating;

import com.github.standobyte.jojo.client.render.entity.model.ownerbound.repeating.RepeatingModel;
import com.hk47bot.rotp_stfn.entity.projectile.ExtendedPunchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ExtendedPunchNewModel<T extends ExtendedPunchEntity> extends RepeatingModel<T> {
    private final ModelRenderer rightForeArm;
    private final ModelRenderer bone3;
    private final ModelRenderer zipper;
    public ExtendedPunchNewModel(){
        texWidth = 128;
        texHeight = 128;

        rightForeArm = new ModelRenderer(this);
        setRotationAngle(rightForeArm, -1.5708F, 0.0F, 1.5708F);
        rightForeArm.texOffs(56, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
        rightForeArm.texOffs(56, 10).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.25F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-2.25F, 1.0F, 0.0F);
        rightForeArm.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.0436F);
        bone3.texOffs(72, 12).addBox(-0.5F, 0.5F, -1.5F, 1.0F, 4.0F, 3.0F, 0.0F, false);
        bone3.texOffs(72, 19).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        zipper = new ModelRenderer(this);
        zipper.texOffs(74, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 6.0F, 0.0F, false);    }
    @Override
    protected ModelRenderer getMainPart() {
        return rightForeArm;
    }

    @Override
    protected float getMainPartLength() {
        return 1F;
    }

    @Override
    protected ModelRenderer getRepeatingPart() {
        return zipper;
    }

    @Override
    protected float getRepeatingPartLength() {
        return 6F;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
