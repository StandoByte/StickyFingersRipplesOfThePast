package com.hk47bot.rotp_stfn.client.render.model.bodypart;

import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedHeadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlayerHeadModel extends EntityModel<UnzippedHeadEntity> {
    protected final ModelRenderer head;
    protected final ModelRenderer hat;

    public PlayerHeadModel() {
        super();
        this.texWidth = 64;
        this.texHeight = 64;
        this.head = new ModelRenderer(this);
        this.head.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.25F);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.hat = new ModelRenderer(this);
        this.hat.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.25F);
        this.hat.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(hat);
    }

    @Override
    public void setupAnim(UnzippedHeadEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        this.head.xRot = xRotation;
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.head.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.hat.render(stack, builder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
