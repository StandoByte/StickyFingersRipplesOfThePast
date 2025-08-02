package com.hk47bot.rotp_stfn.client.render.model.bodypart;

import com.github.standobyte.jojo.util.general.MathUtil;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerLegEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlayerLegModel extends EntityModel<PlayerLegEntity> {
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public final ModelRenderer leftPants;
    public final ModelRenderer rightPants;
    public PlayerLegModel() {
        super();
        this.texWidth = 64;
        this.texHeight = 64;

        this.rightLeg = new ModelRenderer(this, 0, 16);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);

        this.leftLeg = new ModelRenderer(this, 0, 16);
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);

        this.leftPants = new ModelRenderer(this, 0, 48);
        this.leftPants.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F);

        this.rightPants = new ModelRenderer(this, 0, 32);
        this.rightPants.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.25F);
    }

    @Override
    public void setupAnim(PlayerLegEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        rightLeg.xRot = (xRotation + 90) * MathUtil.DEG_TO_RAD;
        leftLeg.xRot = (xRotation + 90) * MathUtil.DEG_TO_RAD;
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.rightLeg.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.rightPants.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.leftLeg.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.leftPants.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
