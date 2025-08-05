package com.hk47bot.rotp_stfn.client.render.renderer.bodypart;

import com.github.standobyte.jojo.util.general.MathUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class HumanoidUtil {
    public static void renderPart(ModelRenderer part, MatrixStack matrixStack, IRenderTypeBuffer buffer, ResourceLocation texture, int packedLight, boolean shouldRotate, float x, float y, float z) {
        part.visible = false;
        part.children.forEach(child -> child.visible = false);
        ModelRenderer copy = part.createShallowCopy();
        copy.cubes = part.cubes;
        copy.children = part.children;
        copy.xRot = shouldRotate ? -90 * MathUtil.DEG_TO_RAD : 0;
        copy.yRot = 0;
        copy.zRot = 0;
        copy.setPos(x, y, z);
        for (ModelRenderer child : copy.children){
            child.render(matrixStack, buffer.getBuffer(RenderType.entityCutout(texture)), packedLight, OverlayTexture.NO_OVERLAY);
        }
        copy.render(matrixStack, buffer.getBuffer(RenderType.entityCutout(texture)), packedLight, OverlayTexture.NO_OVERLAY);
    }
}
