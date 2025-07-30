package com.hk47bot.rotp_stfn.client.render.renderer;

import com.github.standobyte.jojo.entity.stand.StandEntity;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.github.standobyte.jojo.client.ClientUtil.setRotationAngle;
import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.OPEN;
import static net.minecraft.client.renderer.entity.LivingRenderer.getOverlayCoords;

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
        north.setPos(0.0F, 24.0F, 0.0F);
        north.texOffs(0, 0).addBox(0.0F, -24.0F, 15.97F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        corners2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners2.setPos(8.0F, 0.0F, 8.0F);
        north.addChild(corners2);


        right_up2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up2.setPos(-6.5F, -22.5F, 7.8F);
        corners2.addChild(right_up2);
        setRotationAngle(right_up2, 0.0F, 0.0F, -1.5708F);
        right_up2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        left_up2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up2.setPos(6.5F, -22.5F, 7.8F);
        corners2.addChild(left_up2);
        left_up2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        left_down2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down2.setPos(6.5F, -9.5F, 7.8F);
        corners2.addChild(left_down2);
        setRotationAngle(left_down2, 0.0F, 0.0F, 1.5708F);
        left_down2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        right_down2 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down2.setPos(-6.5F, -9.5F, 7.8F);
        corners2.addChild(right_down2);
        setRotationAngle(right_down2, 0.0F, 0.0F, -3.1416F);
        right_down2.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        south = new ModelRenderer(texWidth, texHeight, 0, 0);
        south.setPos(0.0F, 24.0F, 0.0F);
        south.texOffs(0, 0).addBox(0.0F, -24.0F, 0.03F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        corners = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners.setPos(8.0F, 0.0F, 8.0F);
        south.addChild(corners);


        right_up = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up.setPos(6.5F, -22.5F, -7.8F);
        corners.addChild(right_up);
        setRotationAngle(right_up, 0.0F, 0.0F, 1.5708F);
        right_up.texOffs(0, 16).addBox(-1.5F, -1.5F, -0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        left_up = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up.setPos(-6.5F, -22.5F, -7.8F);
        corners.addChild(left_up);
        left_up.texOffs(0, 16).addBox(-1.5F, -1.5F, -0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        left_down = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down.setPos(-6.5F, -9.5F, -7.8F);
        corners.addChild(left_down);
        setRotationAngle(left_down, 0.0F, 0.0F, -1.5708F);
        left_down.texOffs(0, 16).addBox(-1.5F, -1.5F, -0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        right_down = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down.setPos(6.5F, -9.5F, -7.8F);
        corners.addChild(right_down);
        setRotationAngle(right_down, 0.0F, 0.0F, -3.1416F);
        right_down.texOffs(0, 16).addBox(-1.5F, -1.5F, -0.17F, 3.0F, 3.0F, 0.0F, 0.0F, true);

        east = new ModelRenderer(texWidth, texHeight, 0, 0);
        east.setPos(0.0F, 24.0F, 0.0F);
        east.texOffs(0, -16).addBox(0.03F, -24.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);

        corners3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners3.setPos(8.0F, 0.0F, 8.0F);
        east.addChild(corners3);


        right_up3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up3.setPos(-8.0F, -22.5F, -6.6F);
        corners3.addChild(right_up3);
        setRotationAngle(right_up3, 1.5708F, 0.0F, 0.0F);
        right_up3.texOffs(0, 13).addBox(0.03F, -1.4F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        left_up3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up3.setPos(-7.97F, -22.5F, 6.5F);
        corners3.addChild(left_up3);
        left_up3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        left_down3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down3.setPos(-7.97F, -9.4F, 6.5F);
        corners3.addChild(left_down3);
        setRotationAngle(left_down3, -1.5708F, 0.0F, 0.0F);
        left_down3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.6F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        right_down3 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down3.setPos(-7.97F, -9.5F, -6.5F);
        corners3.addChild(right_down3);
        setRotationAngle(right_down3, 3.1416F, 0.0F, 0.0F);
        right_down3.texOffs(0, 13).addBox(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        west = new ModelRenderer(texWidth, texHeight, 0, 0);
        west.setPos(0.0F, 24.0F, 0.0F);
        west.texOffs(0, -16).addBox(15.97F, -24.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);

        corners4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners4.setPos(8.0F, 0.0F, 8.1F);
        west.addChild(corners4);


        right_up4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up4.setPos(7.75F, -22.5F, 6.4F);
        corners4.addChild(right_up4);
        setRotationAngle(right_up4, -1.5708F, 0.0F, 0.0F);
        right_up4.texOffs(0, 13).addBox(0.22F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        left_up4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up4.setPos(7.75F, -22.5F, -6.6F);
        corners4.addChild(left_up4);
        left_up4.texOffs(0, 13).addBox(0.22F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        left_down4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down4.setPos(7.75F, -9.5F, -6.6F);
        corners4.addChild(left_down4);
        setRotationAngle(left_down4, 1.5708F, 0.0F, 0.0F);
        left_down4.texOffs(0, 13).addBox(0.22F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        right_down4 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down4.setPos(7.75F, -9.5F, 6.4F);
        corners4.addChild(right_down4);
        setRotationAngle(right_down4, 3.1416F, 0.0F, 0.0F);
        right_down4.texOffs(0, 13).addBox(0.22F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        up = new ModelRenderer(texWidth, texHeight, 0, 0);
        up.setPos(0.0F, 24.0F, 0.0F);
        up.texOffs(-16, 0).addBox(0.0F, -23.97F, 0.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);

        corners5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners5.setPos(8.0F, 0.0F, 8.0F);
        up.addChild(corners5);


        right_up5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up5.setPos(-6.5F, -23.9F, -6.6F);
        corners5.addChild(right_up5);
        setRotationAngle(right_up5, 0.0F, -1.5708F, 0.0F);
        right_up5.texOffs(-3, 16).addBox(-1.4F, -0.07F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_up5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up5.setPos(6.5F, -23.9F, -6.6F);
        corners5.addChild(left_up5);
        setRotationAngle(left_up5, 0.0F, 3.1416F, 0.0F);
        left_up5.texOffs(-3, 16).addBox(-1.5F, -0.07F, -1.6F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_down5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down5.setPos(6.5F, -23.9F, 6.4F);
        corners5.addChild(left_down5);
        setRotationAngle(left_down5, 0.0F, 1.5708F, 0.0F);
        left_down5.texOffs(-3, 16).addBox(-1.6F, -0.07F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        right_down5 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down5.setPos(-6.5F, -23.9F, 6.4F);
        corners5.addChild(right_down5);
        right_down5.texOffs(-3, 16).addBox(-1.5F, -0.07F, -1.4F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        down = new ModelRenderer(texWidth, texHeight, 0, 0);
        down.setPos(0.0F, 24.0F, 0.0F);
        down.texOffs(-16, 0).addBox(0.0F, -8.03F, 0.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);

        corners6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        corners6.setPos(8.0F, 0.0F, 8.0F);
        down.addChild(corners6);


        right_up6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_up6.setPos(6.5F, -8.1F, -6.6F);
        corners6.addChild(right_up6);
        setRotationAngle(right_up6, 0.0F, 3.1416F, 0.0F);
        right_up6.texOffs(-3, 16).addBox(-1.5F, 0.07F, -1.6F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_up6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_up6.setPos(-6.5F, -8.1F, -6.6F);
        corners6.addChild(left_up6);
        setRotationAngle(left_up6, 0.0F, -1.5708F, 0.0F);
        left_up6.texOffs(-3, 16).addBox(-1.4F, 0.07F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        left_down6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        left_down6.setPos(-6.5F, -8.1F, 6.4F);
        corners6.addChild(left_down6);
        left_down6.texOffs(-3, 16).addBox(-1.5F, 0.07F, -1.4F, 3.0F, 0.0F, 3.0F, 0.0F, false);

        right_down6 = new ModelRenderer(texWidth, texHeight, 0, 0);
        right_down6.setPos(6.5F, -8.1F, 6.4F);
        corners6.addChild(right_down6);
        setRotationAngle(right_down6, 0.0F, 1.5708F, 0.0F);
        right_down6.texOffs(-3, 16).addBox(-1.6F, 0.07F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, false);
    }

    public void renderFaces(StickyFingersZipperTileEntity zipper, float partialTick, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, int packedOverlay){
        Direction direction;
        ZipperFace face;
        if (zipper.shouldRenderFace(Direction.NORTH)){
            direction = Direction.NORTH;
            face = zipper.getFaceValue(direction);
            corners2.visible = zipper.getBlockState().getValue(OPEN);
            right_up2.visible = face.isRightDown();
            right_down2.visible = face.isRightUp();
            left_up2.visible = face.isLeftDown();
            left_down2.visible = face.isLeftUp();
            north.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), packedLight, packedOverlay);
        }
        if (zipper.shouldRenderFace(Direction.SOUTH)){
            direction = Direction.SOUTH;
            face = zipper.getFaceValue(direction);
            corners.visible = zipper.getBlockState().getValue(OPEN);
            right_up.visible = face.isLeftDown();
            right_down.visible = face.isLeftUp();
            left_up.visible = face.isRightDown();
            left_down.visible = face.isRightUp();
            south.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), packedLight, packedOverlay);
        }
        if (zipper.shouldRenderFace(Direction.EAST)){
            direction = Direction.EAST;
            face = zipper.getFaceValue(direction);
            corners3.visible = zipper.getBlockState().getValue(OPEN);
            right_up3.visible = face.isLeftDown();
            right_down3.visible = face.isLeftUp();
            left_up3.visible = face.isRightDown();
            left_down3.visible = face.isRightUp();
            east.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), packedLight, packedOverlay);
        }
        if (zipper.shouldRenderFace(Direction.WEST)){
            direction = Direction.WEST;
            face = zipper.getFaceValue(direction);
            corners4.visible = zipper.getBlockState().getValue(OPEN);
            right_up4.visible = face.isRightDown();
            right_down4.visible = face.isRightUp();
            left_up4.visible = face.isLeftDown();
            left_down4.visible = face.isLeftUp();
            west.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), packedLight, packedOverlay);
        }
        if (zipper.shouldRenderFace(Direction.UP)){
            direction = Direction.UP;
            face = zipper.getFaceValue(direction);
            corners5.visible = zipper.getBlockState().getValue(OPEN);
            right_up5.visible = face.isLeftDown();
            right_down5.visible = face.isLeftUp();
            left_up5.visible = face.isRightDown();
            left_down5.visible = face.isRightUp();
            up.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), packedLight, packedOverlay);
        }
        if (zipper.shouldRenderFace(Direction.DOWN)){
            direction = Direction.DOWN;
            face = zipper.getFaceValue(direction);
            corners6.visible = zipper.getBlockState().getValue(OPEN);
            right_up6.visible = face.isRightDown();
            right_down6.visible = face.isRightUp();
            left_up6.visible = face.isLeftDown();
            left_down6.visible = face.isLeftUp();
            down.render(stack, buffer.getBuffer(RenderType.entityCutout(zipper.getFaceTexture(zipper.getFaceValue(direction)))), packedLight, packedOverlay);
        }
    }

    private static final float OVERLAY_TICKS = 10.0F;

    public static int getOverlayCoords(float partialTick) {
        return OverlayTexture.pack(OverlayTexture.u(partialTick), OverlayTexture.v(false));
    }

    protected float getWhiteOverlayProgress(StickyFingersZipperTileEntity entity, float partialTick) {
        return entity.overlayTickCount > OVERLAY_TICKS ? 0 :
                (OVERLAY_TICKS - MathHelper.clamp(entity.overlayTickCount + partialTick, 0.0F, OVERLAY_TICKS)) / OVERLAY_TICKS;
    }

    @Override
    public void render(StickyFingersZipperTileEntity zipper, float partialTick, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, int packedOverlay) {
        int whiteOverlay = getOverlayCoords(getWhiteOverlayProgress(zipper, partialTick));
        this.renderFaces(zipper, partialTick, stack, buffer, packedLight, whiteOverlay);
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
        float red = (0.75f + RANDOM.nextFloat() * 0.05f) * colorIntensity;
        float green = (0.35f + RANDOM.nextFloat() * 0.05f) * colorIntensity;
        float blue = (0.90f + RANDOM.nextFloat() * 0.05f) * colorIntensity;

        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, 0.0F, 1.0F, uvAnimationOffset, uvAnimationOffset, uvAnimationOffset, uvAnimationOffset, red, green, blue, Direction.SOUTH);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, red, green, blue, Direction.NORTH);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, uvAnimationOffset, uvAnimationOffset, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.EAST);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.WEST);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, 1.0F-uvAnimationOffset, 1.0F-uvAnimationOffset, 0.0F, 0.0F, 1.0F, 1.0F, red, green, blue, Direction.DOWN);
        this.renderFace(tileEntity, poseMatrix, vertexBuffer, 0.0F, 1.0F, uvAnimationOffset, uvAnimationOffset, 1.0F, 1.0F, 0.0F, 0.0F, red, green, blue, Direction.UP);
    }

    private void renderFace(StickyFingersZipperTileEntity tileEntity, Matrix4f poseMatrix, IVertexBuilder vertexBuffer, float startX, float endX, float startY, float endY, float z1, float z2, float z3, float z4, float red, float green, float blue, Direction direction) {
        if (tileEntity.shouldRenderFace(direction)) {
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
        return 0.001F;
    }

    public static class StickyFingersZipperRenderType extends RenderType {

        public StickyFingersZipperRenderType(String name, VertexFormat format, int mode, int bufferSize,
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
