package com.hk47bot.rotp_stfn.client;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.client.render.renderer.StickyFingersZipperBlockRenderer;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.hk47bot.rotp_stfn.client.render.renderer.StickyFingersZipperBlockRenderer.END_PORTAL_LOCATION;
import static com.hk47bot.rotp_stfn.client.render.renderer.StickyFingersZipperBlockRenderer.END_SKY_LOCATION;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        if (ZipperUtil.hasZippersAround(event.getBlockPos(), mc.level)) {
            event.setCanceled(true);
            renderEndSky(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void clientTick(TickEvent.ClientTickEvent event){
        if (mc.player != null){
            mc.player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(EntityZipperCapability::tick);
        }
    }

    private static final Random RANDOM = new Random(31101L);

    private static void renderEndSky(RenderBlockOverlayEvent event){
        RenderSystem.enableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float red = (0.75f + RANDOM.nextFloat() * 0.05f) * 0.15F;
        float green = (0.35f + RANDOM.nextFloat() * 0.05f) * 0.15F;
        float blue = (0.90f + RANDOM.nextFloat() * 0.05f) * 0.15F;
        Matrix4f matrix4f = event.getMatrixStack().last().pose();
        RenderType enderSkyRenderType = StickyFingersZipperBlockRenderer.StickyFingersZipperRenderType.endPortalNoFog(0);
        IVertexBuilder skyBuffer = mc.renderBuffers().bufferSource().getBuffer(enderSkyRenderType);
        event.getMatrixStack().pushPose();
        skyBuffer.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(red, green, blue, 1F).uv(4.0F, 4.0F).endVertex();
        skyBuffer.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(red, green, blue, 1F).uv(0.0F, 4.0F).endVertex();
        skyBuffer.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(red, green, blue, 1F).uv(0.0F, 0.0F).endVertex();
        skyBuffer.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(red, green, blue, 1F).uv(4.0F, 0.0F).endVertex();
        event.getMatrixStack().popPose();
        for (int i = 1; i <= 16; i++) {
            red = (0.75f + RANDOM.nextFloat() * 0.05f) * 2.0F / (float)(19 - i);
            green = (0.35f + RANDOM.nextFloat() * 0.05f) * 2.0F / (float)(19 - i);
            blue = (0.90f + RANDOM.nextFloat() * 0.05f) * 2.0F / (float)(19 - i);
            RenderType enderRenderType = StickyFingersZipperBlockRenderer.StickyFingersZipperRenderType.endPortalNoFog(i);
            IVertexBuilder buffer = mc.renderBuffers().bufferSource().getBuffer(enderRenderType);
            RenderSystem.defaultBlendFunc();
            event.getMatrixStack().pushPose();
            buffer.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(red, green, blue, 1F).uv(4.0F, 4.0F).endVertex();
            buffer.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(red, green, blue, 1F).uv(0.0F, 4.0F).endVertex();
            buffer.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(red, green, blue, 1F).uv(0.0F, 0.0F).endVertex();
            buffer.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(red, green, blue, 1F).uv(4.0F, 0.0F).endVertex();
            event.getMatrixStack().popPose();
        }
        RenderSystem.disableBlend();
    }
}
