package com.hk47bot.rotp_stfn.client.render.model;

import com.hk47bot.rotp_stfn.entity.PlayerHeadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlayerHeadModel extends EntityModel<PlayerHeadEntity> {
    private final ModelRenderer hat = new ModelRenderer(this, 32, 0);
    protected final ModelRenderer head= new ModelRenderer(this, 0, 0);;

    public PlayerHeadModel() {
        this.hat.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.25F);
        this.hat.setPos(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setupAnim(PlayerHeadEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        this.hat.yRot = this.head.yRot;
        this.hat.xRot = this.head.xRot;
    }

    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        this.head.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        this.hat.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
