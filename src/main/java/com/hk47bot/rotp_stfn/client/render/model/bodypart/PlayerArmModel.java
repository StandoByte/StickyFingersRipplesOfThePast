package com.hk47bot.rotp_stfn.client.render.model.bodypart;

import com.github.standobyte.jojo.util.general.MathUtil;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedArmEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlayerArmModel extends EntityModel<UnzippedArmEntity> {
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;
    public ModelRenderer rightSleeve;
    public ModelRenderer leftSleeve;
    public PlayerArmModel() {
        super();
        this.texWidth = 64;
        this.texHeight = 64;

        this.rightArm = new ModelRenderer(this, 40, 16);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        this.rightArm.setPos(1.0F, 0.0F, 0.0F);

        this.leftArm = new ModelRenderer(this, 32, 48);
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        this.leftArm.setPos(1.0F, 0.0F, 0.0F);

        this.rightSleeve = new ModelRenderer(this, 40, 32);
        this.rightSleeve.addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F);
        this.rightSleeve.setPos(1.0F, 0.0F, 0.0F);

        this.leftSleeve = new ModelRenderer(this, 48, 48);
        this.leftSleeve.addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.25F);
        this.leftSleeve.setPos(1.0F, 0.0F, 0.0F);
    }

    @Override
    public void setupAnim(UnzippedArmEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        rightArm.xRot = (xRotation + 90) * MathUtil.DEG_TO_RAD;
        leftArm.xRot = (xRotation + 90) * MathUtil.DEG_TO_RAD;
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.rightArm.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.rightSleeve.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.leftArm.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.leftSleeve.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }

}
