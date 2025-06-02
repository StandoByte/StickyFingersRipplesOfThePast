package com.hk47bot.rotp_stfn.client.render.renderer;

import com.google.common.collect.ImmutableList;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.ZipperFace;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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

import static com.github.standobyte.jojo.client.ClientUtil.setRotationAngle;
import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.OPEN;

public class StickyFingersZipperBlockRenderer extends TileEntityRenderer<StickyFingersZipperTileEntity> {
    private final ModelRenderer north;
    private final ModelRenderer right_up;
    private final ModelRenderer left_up;
    private final ModelRenderer left_down;
    private final ModelRenderer right_down;
    private final ModelRenderer south;
    private final ModelRenderer right_up2;
    private final ModelRenderer left_up2;
    private final ModelRenderer left_down2;
    private final ModelRenderer right_down2;
    private final ModelRenderer east;
    private final ModelRenderer right_up3;
    private final ModelRenderer left_up3;
    private final ModelRenderer left_down3;
    private final ModelRenderer right_down3;
    private final ModelRenderer west;
    private final ModelRenderer right_up4;
    private final ModelRenderer left_up4;
    private final ModelRenderer left_down4;
    private final ModelRenderer right_down4;
    private final ModelRenderer up;
    private final ModelRenderer right_up5;
    private final ModelRenderer left_up5;
    private final ModelRenderer left_down5;
    private final ModelRenderer right_down5;
    private final ModelRenderer down;
    private final ModelRenderer right_up6;
    private final ModelRenderer left_up6;
    private final ModelRenderer left_down6;
    private final ModelRenderer right_down6;
    private final ModelRenderer corners;
    private final ModelRenderer corners2;
    private final ModelRenderer corners3;
    private final ModelRenderer corners4;
    private final ModelRenderer corners5;
    private final ModelRenderer corners6;

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

        corners2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners2.setPos(-16.0F, 0.0F, 31.7F);
        setRotationAngle(corners2, 0.0F, (float) Math.toRadians(180D), 0F);
        north.addChild(corners2);


        right_up2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up2.setPos(-14.5F, -14.5F, 15.8F);
        corners2.addChild(right_up2);
        setRotationAngle(right_up2, 0.0F, 0.0F, -1.5708F);
        right_up2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        left_up2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up2.setPos(-1.5F, -14.5F, 15.8F);
        corners2.addChild(left_up2);
        left_up2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        left_down2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down2.setPos(-1.5F, -1.5F, 15.8F);
        corners2.addChild(left_down2);
        setRotationAngle(left_down2, 0.0F, 0.0F, 1.5708F);
        left_down2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        right_down2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down2.setPos(-14.5F, -1.5F, 15.8F);
        corners2.addChild(right_down2);
        setRotationAngle(right_down2, 0.0F, 0.0F, -3.1416F);
        right_down2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);


        south = new ModelRenderer(texWidth, texHeight, 0, 0);
        south.setPos(16.0F, 16, 0.0F);
        south.texOffs(0, 0).addBox(-16.0F, -16.0F, 0.03F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        corners = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners.setPos(-16.0F, 0.0F, 0.2F);
        setRotationAngle(corners, 0.0F, (float) Math.toRadians(180D), 0F);
        south.addChild(corners);


        right_up = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up.setPos(-1.5F, -14.5F, 0.0F);
        corners.addChild(right_up);
        setRotationAngle(right_up, 0.0F, 0.0F, 1.5708F);
        right_up.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        left_up = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up.setPos(-14.5F, -14.5F, 0.0F);
        corners.addChild(left_up);
        left_up.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        left_down = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down.setPos(-14.5F, -1.5F, 0.0F);
        corners.addChild(left_down);
        setRotationAngle(left_down, 0.0F, 0.0F, -1.5708F);
        left_down.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        right_down = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down.setPos(-1.5F, -1.5F, 0.0F);
        corners.addChild(right_down);
        setRotationAngle(right_down, 0.0F, 0.0F, -3.1416F);
        right_down.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);


        east = new ModelRenderer(texWidth, texHeight, 0, 0);
        east.setPos(16.0F, 16, 0.0F);
        east.texOffs(0, -16).addBox(-15.97F, -16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);

        corners3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners3.setPos(-31.8F, 0.0F, 16.0F);
        setRotationAngle(corners3, 0.0F, (float) Math.toRadians(180D), 0F);
        east.addChild(corners3);


        right_up3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up3.setPos(-15.9F, -14.5F, 1.4F);
        corners3.addChild(right_up3);
        setRotationAngle(right_up3, 1.5708F, 0.0F, 0.0F);
        right_up3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        left_up3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up3.setPos(-15.9F, -14.5F, 14.4F);
        corners3.addChild(left_up3);
        left_up3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        left_down3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down3.setPos(-15.9F, -1.5F, 14.4F);
        corners3.addChild(left_down3);
        setRotationAngle(left_down3, -1.5708F, 0.0F, 0.0F);
        left_down3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        right_down3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down3.setPos(-15.9F, -1.5F, 1.4F);
        corners3.addChild(right_down3);
        setRotationAngle(right_down3, 3.1416F, 0.0F, 0.0F);
        right_down3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        west = new ModelRenderer(texWidth, texHeight, 0, 0);
        west.setPos(16.0F, 16, 0.0F);
        west.texOffs(0, -16).addBox(-0.03F, -16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);

        corners4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners4.setPos(-0.15F, 0F, 16.0F);
        setRotationAngle(corners4, 0.0F, (float) Math.toRadians(180D), 0F);
        west.addChild(corners4);


        right_up4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up4.setPos(-0.1F, -14.5F, 14.4F);
        corners4.addChild(right_up4);
        setRotationAngle(right_up4, -1.5708F, 0.0F, 0.0F);
        right_up4.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        left_up4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up4.setPos(-0.1F, -14.5F, 1.4F);
        corners4.addChild(left_up4);
        left_up4.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        left_down4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down4.setPos(-0.1F, -1.5F, 1.4F);
        corners4.addChild(left_down4);
        setRotationAngle(left_down4, 1.5708F, 0.0F, 0.0F);
        left_down4.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        right_down4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down4.setPos(-0.1F, -1.5F, 14.4F);
        corners4.addChild(right_down4);
        setRotationAngle(right_down4, 3.1416F, 0.0F, 0.0F);
        right_down4.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        up = new ModelRenderer(texWidth, texHeight, 0, 0);
        up.setPos(16.0F, 16, 0.0F);
        up.texOffs(-16, 0).addBox(-16.0F, -15.97F, 0.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);

        corners5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners5.setPos(0.0F, 0.0F, 0F);
        up.addChild(corners5);


        right_up5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up5.setPos(-14.5F, -15.9F, 1.4F);
        corners5.addChild(right_up5);
        setRotationAngle(right_up5, 0.0F, -1.5708F, 0.0F);
        right_up5.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_up5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up5.setPos(-1.5F, -15.9F, 1.4F);
        corners5.addChild(left_up5);
        setRotationAngle(left_up5, 0.0F, 3.1416F, 0.0F);
        left_up5.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_down5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down5.setPos(-1.5F, -15.9F, 14.4F);
        corners5.addChild(left_down5);
        setRotationAngle(left_down5, 0.0F, 1.5708F, 0.0F);
        left_down5.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        right_down5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down5.setPos(-14.5F, -15.9F, 14.4F);
        corners5.addChild(right_down5);
        right_down5.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        down = new ModelRenderer(texWidth, texHeight, 0, 0);
        down.setPos(16.0F, 16, 0.0F);
        down.texOffs(-16, 0).addBox(-16.0F, -0.03F, 0.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);

        corners6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners6.setPos(0.0F, 0.0F, 0F);
        down.addChild(corners6);


        right_up6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up6.setPos(-1.5F, -0.1F, 1.4F);
        corners6.addChild(right_up6);
        setRotationAngle(right_up6, 0.0F, 3.1416F, 0.0F);
        right_up6.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_up6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up6.setPos(-14.5F, -0.1F, 1.4F);
        corners6.addChild(left_up6);
        setRotationAngle(left_up6, 0.0F, -1.5708F, 0.0F);
        left_up6.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_down6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down6.setPos(-14.5F, -0.1F, 14.4F);
        corners6.addChild(left_down6);
        left_down6.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        right_down6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down6.setPos(-1.5F, -0.1F, 14.4F);
        corners6.addChild(right_down6);
        setRotationAngle(right_down6, 0.0F, 1.5708F, 0.0F);
        right_down6.texOffs(-3, 16).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
    }


    @Override
    public void render(StickyFingersZipperTileEntity zipper, float p_225616_2_, MatrixStack stack, IRenderTypeBuffer buffer, int p_225616_5_, int p_225616_6_) {
        Direction direction;
        ZipperFace face;
        if (zipper.shouldRenderFace(Direction.NORTH)){
            direction = Direction.NORTH;
            north.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), p_225616_5_, p_225616_6_);
            face = zipper.getFaceValue(direction);
            corners2.visible = zipper.getBlockState().getValue(OPEN);
            right_up2.visible = face.isLeftDown();
            right_down2.visible = face.isLeftUp();
            left_up2.visible = face.isRightDown();
            left_down2.visible = face.isRightUp();
        }
        if (zipper.shouldRenderFace(Direction.SOUTH)){
            direction = Direction.SOUTH;
            south.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), p_225616_5_, p_225616_6_);
            face = zipper.getFaceValue(direction);
            corners.visible = zipper.getBlockState().getValue(OPEN);
            right_up.visible = face.isLeftDown();
            right_down.visible = face.isLeftUp();
            left_up.visible = face.isRightDown();
            left_down.visible = face.isRightUp();
        }
        if (zipper.shouldRenderFace(Direction.EAST)){
            direction = Direction.EAST;
            east.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), p_225616_5_, p_225616_6_);
            face = zipper.getFaceValue(direction);
            corners3.visible = zipper.getBlockState().getValue(OPEN);
            right_up3.visible = face.isLeftDown();
            right_down3.visible = face.isLeftUp();
            left_up3.visible = face.isRightDown();
            left_down3.visible = face.isRightUp();
        }
        if (zipper.shouldRenderFace(Direction.WEST)){
            direction = Direction.WEST;
            west.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), p_225616_5_, p_225616_6_);
            face = zipper.getFaceValue(direction);
            corners4.visible = zipper.getBlockState().getValue(OPEN);
            right_up4.visible = face.isLeftDown();
            right_down4.visible = face.isLeftUp();
            left_up4.visible = face.isRightDown();
            left_down4.visible = face.isRightUp();
        }
        if (zipper.shouldRenderFace(Direction.UP)){
            direction = Direction.UP;
            up.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), p_225616_5_, p_225616_6_);
            face = zipper.getFaceValue(direction);
            corners5.visible = zipper.getBlockState().getValue(OPEN);
            right_up5.visible = face.isLeftDown();
            right_down5.visible = face.isLeftUp();
            left_up5.visible = face.isRightDown();
            left_down5.visible = face.isRightUp();
        }
        if (zipper.shouldRenderFace(Direction.DOWN)){
            direction = Direction.DOWN;
            down.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), p_225616_5_, p_225616_6_);
            face = zipper.getFaceValue(direction);
            corners6.visible = zipper.getBlockState().getValue(OPEN);
            right_up6.visible = face.isLeftDown();
            right_down6.visible = face.isLeftUp();
            left_up6.visible = face.isRightDown();
            left_down6.visible = face.isRightUp();
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
                textureState = new RenderState.TextureState(END_SKY_LOCATION, true, false);
            } else {
                transparencyState = RenderState.ADDITIVE_TRANSPARENCY;
                textureState = new RenderState.TextureState(END_PORTAL_LOCATION, true, false);
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
