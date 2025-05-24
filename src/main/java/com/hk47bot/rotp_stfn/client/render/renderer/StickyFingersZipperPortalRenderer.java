package com.hk47bot.rotp_stfn.client.render.renderer;

import com.google.common.collect.ImmutableList;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class StickyFingersZipperPortalRenderer<T extends StickyFingersZipperTileEntity> extends TileEntityRenderer<T> {
    public static final ResourceLocation END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
    public static final ResourceLocation END_PORTAL_LOCATION = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/zipper/effect/zipper_void.png");
    private static final Random RANDOM = new Random(31100L);
    private static final List<RenderType> RENDER_TYPES = IntStream.range(0, 16).mapToObj((p_228882_0_) -> {
        return StickyFingersZipperRenderType.endPortalNoFog(p_228882_0_ + 1);
    }).collect(ImmutableList.toImmutableList());

    public StickyFingersZipperPortalRenderer(TileEntityRendererDispatcher p_i226019_1_) {
        super(p_i226019_1_);
    }

    public void render(T p_225616_1_, float p_225616_2_, MatrixStack p_225616_3_, IRenderTypeBuffer p_225616_4_, int p_225616_5_, int p_225616_6_) {
        RANDOM.setSeed(31101L);
        double d0 = p_225616_1_.getBlockPos().distSqr(this.renderer.camera.getPosition(), true);
        int i = this.getPasses(d0);
        float f = this.getOffset();
        Matrix4f matrix4f = p_225616_3_.last().pose();
        this.renderCube(p_225616_1_, f, 0.15F, matrix4f, p_225616_4_.getBuffer(RENDER_TYPES.get(0)));

        for(int j = 1; j < i; ++j) {
            this.renderCube(p_225616_1_, f, 2.0F / (float)(18 - j), matrix4f, p_225616_4_.getBuffer(RENDER_TYPES.get(j)));
        }

    }

    private void renderCube(T tileEntity, float uvAnimationOffset, float colorIntensity, Matrix4f poseMatrix, IVertexBuilder vertexBuffer) {
        // Генерация случайных цветовых компонентов с учётом интенсивности
//        float red = (RANDOM.nextFloat() * 0.8F + 0.1F) * colorIntensity;
//        float green = 0;
//        float blue = (RANDOM.nextFloat() * 0.6F + 0.5F) * colorIntensity;
        float red = (0.75f + RANDOM.nextFloat() * 0.05f) * colorIntensity;
        float green = (0.35f + RANDOM.nextFloat() * 0.05f) * colorIntensity;
        float blue = (0.90f + RANDOM.nextFloat() * 0.05f) * colorIntensity;

        // Отрисовка всех граней куба
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, 0.0F, 1.0F, uvAnimationOffset, uvAnimationOffset, uvAnimationOffset, uvAnimationOffset, red, green, blue, Direction.SOUTH);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, red, green, blue, Direction.NORTH);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, uvAnimationOffset, uvAnimationOffset, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.EAST);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.WEST);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 0.0F, 0.0F, 1.0F, 1.0F, red, green, blue, Direction.DOWN);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, uvAnimationOffset, uvAnimationOffset, 1.0F, 1.0F, 0.0F, 0.0F, red, green, blue, Direction.UP);
    }

    private void renderFace(T tileEntity, Matrix4f poseMatrix, IVertexBuilder vertexBuffer, float startX, float endX, float startY, float endY, float z1, float z2, float z3, float z4, float red, float green, float blue, Direction direction) {
        if (tileEntity.shouldRenderFace(direction)) {
            // Вершины грани (порядок: нижний левый, нижний правый, верхний правый, верхний левый)
            vertexBuffer.vertex(poseMatrix, startX, startY, z1).color(red, green, blue, 1.0F).endVertex();
            vertexBuffer.vertex(poseMatrix, endX, startY, z2).color(red, green, blue, 1.0F).endVertex();
            vertexBuffer.vertex(poseMatrix, endX, endY, z3).color(red, green, blue, 1.0F).endVertex();
            vertexBuffer.vertex(poseMatrix, startX, endY, z4).color(red, green, blue, 1.0F).endVertex();
        }
    }

    protected int getPasses(double p_191286_1_) {
        if (p_191286_1_ > 36864.0D) {
            return 1;
        } else if (p_191286_1_ > 25600.0D) {
            return 3;
        } else if (p_191286_1_ > 16384.0D) {
            return 5;
        } else if (p_191286_1_ > 9216.0D) {
            return 7;
        } else if (p_191286_1_ > 4096.0D) {
            return 9;
        } else if (p_191286_1_ > 1024.0D) {
            return 11;
        } else if (p_191286_1_ > 576.0D) {
            return 13;
        } else {
            return p_191286_1_ > 256.0D ? 14 : 15;
        }
    }

    protected float getOffset() {
        return 0.025F;
    }

    private static class StickyFingersZipperRenderType extends RenderType {

        private StickyFingersZipperRenderType(String name, VertexFormat format, int mode, int bufferSize,
                                       boolean affectCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
            super(name, format, mode, bufferSize, affectCrumbling, sortOnUpload, setupState, clearState);
        }

        public static RenderType endPortalNoFog(int i) {
            RenderState.TransparencyState transparencyState;
            RenderState.TextureState textureState;
            if (i <= 1) {
                transparencyState = RenderState.TRANSLUCENT_TRANSPARENCY;
                textureState = new RenderState.TextureState(StickyFingersZipperPortalRenderer.END_SKY_LOCATION, true, false);
            } else {
                transparencyState = RenderState.ADDITIVE_TRANSPARENCY;
                textureState = new RenderState.TextureState(StickyFingersZipperPortalRenderer.END_PORTAL_LOCATION, true, false);
            }

            return RenderType.create("rotp_stfn_portal", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, RenderType.State.builder()
                    .setTransparencyState(transparencyState)
                    .setTextureState(textureState)
                    .setTexturingState(new RenderState.PortalTexturingState(i))
                    .setFogState(RenderState.NO_FOG)
                    .createCompositeState(false));
        }
    }

}
