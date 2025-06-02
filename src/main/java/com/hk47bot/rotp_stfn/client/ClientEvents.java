package com.hk47bot.rotp_stfn.client;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.hk47bot.rotp_stfn.client.render.renderer.StickyFingersZipperBlockRenderer.END_PORTAL_LOCATION;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event){
        if (ZipperUtil.hasZippersAround(event.getBlockPos(), event.getPlayer().level)){
            event.setCanceled(true);
            renderPortal(mc, event.getMatrixStack());
        }
    }
    private static void renderPortal(Minecraft p_228736_0_, MatrixStack p_228736_1_) {
        RenderSystem.enableTexture();
        p_228736_0_.getTextureManager().bind(END_PORTAL_LOCATION);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        float f = p_228736_0_.player.getBrightness();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float f1 = 4.0F;
        float f2 = -1.0F;
        float f3 = 1.0F;
        float f4 = -1.0F;
        float f5 = 1.0F;
        float f6 = -0.5F;
        float f7 = -p_228736_0_.player.yRot / 64.0F;
        float f8 = p_228736_0_.player.xRot / 64.0F;
        Matrix4f matrix4f = p_228736_1_.last().pose();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, 0.1F).uv(4.0F + f7, 4.0F + f8).endVertex();
        bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, 0.1F).uv(0.0F + f7, 4.0F + f8).endVertex();
        bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, 0.1F).uv(0.0F + f7, 0.0F + f8).endVertex();
        bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, 0.1F).uv(4.0F + f7, 0.0F + f8).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.disableBlend();
    }
}
