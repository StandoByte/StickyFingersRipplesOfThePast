package com.hk47bot.rotp_stfn.client.render.renderer;

import com.github.standobyte.jojo.client.ClientUtil;
import com.google.common.collect.ImmutableList;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
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

import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.OPEN;

public class StickyFingersZipperBlockRenderer extends TileEntityRenderer<StickyFingersZipperTileEntity> {
    private final ModelRenderer north;
    private final ModelRenderer south;
    private final ModelRenderer east;
    private final ModelRenderer west;
    private final ModelRenderer up;
    private final ModelRenderer down;

    public static final ResourceLocation END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
    public static final ResourceLocation END_PORTAL_LOCATION = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/zipper/effect/zipper_void.png");
    private static final Random RANDOM = new Random(31100L);
    private static final List<RenderType> RENDER_TYPES = IntStream.range(0, 16).mapToObj((p_228882_0_) -> {
        return StickyFingersZipperBlockRenderer.StickyFingersZipperRenderType.endPortalNoFog(p_228882_0_ + 1);
    }).collect(ImmutableList.toImmutableList());

    public StickyFingersZipperBlockRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
        int texWidth = 32;
        int texHeight = 32;
        north = new ModelRenderer(texWidth, texHeight, 0, 0);
        north.setPos(16.0F, 16, 0.0F);
        north.texOffs(0, 0).addBox(-16.0F, -16.0F, 15.97F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        south = new ModelRenderer(texWidth, texHeight, 0, 0);
        south.setPos(16.0F, 16, 0.0F);
        south.texOffs(0, 0).addBox(-16.0F, -16.0F, 0.03F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        east = new ModelRenderer(texWidth, texHeight, 0, 0);
        east.setPos(16.0F, 16, 0.0F);
        east.texOffs(0, -16).addBox(-15.97F, -16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);

        west = new ModelRenderer(texWidth, texHeight, 0, 0);
        west.setPos(16.0F, 16, 0.0F);
        west.texOffs(0, -16).addBox(-0.03F, -16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);

        up = new ModelRenderer(texWidth, texHeight, 0, 0);
        up.setPos(16.0F, 16, 0.0F);
        up.texOffs(-16, 0).addBox(-16.0F, -15.97F, 0.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);

        down = new ModelRenderer(texWidth, texHeight, 0, 0);
        down.setPos(16.0F, 16, 0.0F);
        down.texOffs(-16, 0).addBox(-16.0F, -0.03F, 0.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);
    }
    @Override
    public void render(StickyFingersZipperTileEntity zipper, float p_225616_2_, MatrixStack stack, IRenderTypeBuffer buffer, int p_225616_5_, int p_225616_6_) {
        if (zipper.shouldRenderFace(Direction.NORTH)){
            north.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.FACES.stream().filter(face -> face.getDirection() == Direction.SOUTH).findFirst().get()))), p_225616_5_, p_225616_6_);
        }
        if (zipper.shouldRenderFace(Direction.SOUTH)){
            south.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.FACES.stream().filter(face -> face.getDirection() == Direction.NORTH).findFirst().get()))), p_225616_5_, p_225616_6_);
        }
        if (zipper.shouldRenderFace(Direction.EAST)){
            east.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.FACES.stream().filter(face -> face.getDirection() == Direction.EAST).findFirst().get()))), p_225616_5_, p_225616_6_);
        }
        if (zipper.shouldRenderFace(Direction.WEST)){
            west.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.FACES.stream().filter(face -> face.getDirection() == Direction.WEST).findFirst().get()))), p_225616_5_, p_225616_6_);
        }
        if (zipper.shouldRenderFace(Direction.UP)){
            up.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.FACES.stream().filter(face -> face.getDirection() == Direction.UP).findFirst().get()))), p_225616_5_, p_225616_6_);
        }
        if (zipper.shouldRenderFace(Direction.DOWN)){
            down.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.FACES.stream().filter(face -> face.getDirection() == Direction.DOWN).findFirst().get()))), p_225616_5_, p_225616_6_);
        }
        if (zipper.getBlockState().getValue(OPEN)){
            RANDOM.setSeed(31101L);
            double d0 = zipper.getBlockPos().distSqr(this.renderer.camera.getPosition(), true);
            int i = this.getPasses(d0);
            float f = this.getOffset();
            Matrix4f matrix4f = stack.last().pose();
            this.renderCube(zipper, f, 0.15F, matrix4f, buffer.getBuffer(RENDER_TYPES.get(0)));

            for(int j = 1; j < i; ++j) {
                this.renderCube(zipper, f, 2.0F / (float)(18 - j), matrix4f, buffer.getBuffer(RENDER_TYPES.get(j)));
            }
        }
    }

    private void renderCube(StickyFingersZipperTileEntity tileEntity, float uvAnimationOffset, float colorIntensity, Matrix4f poseMatrix, IVertexBuilder vertexBuffer) {
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

    private void renderFace(StickyFingersZipperTileEntity tileEntity, Matrix4f poseMatrix, IVertexBuilder vertexBuffer, float startX, float endX, float startY, float endY, float z1, float z2, float z3, float z4, float red, float green, float blue, Direction direction) {
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
        return 0.0004F;
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
