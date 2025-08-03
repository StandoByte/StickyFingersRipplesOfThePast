package com.hk47bot.rotp_stfn.client;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.ui.screen.widgets.ImageVanillaButton;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClAngeloRockButtonPacket;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.client.render.renderer.tileentity.StickyFingersZipperBlockRenderer;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import com.hk47bot.rotp_stfn.init.InitEntities;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.HeadRespawnPacket;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private ClientEvents(Minecraft mc){
        ClientEvents.mc = mc;
    }

    public static void init(Minecraft mc){
        MinecraftForge.EVENT_BUS.register(new ClientEvents(mc));
    }

    private static Minecraft mc;

    @SubscribeEvent
    public static void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        if (ZipperUtil.hasZippersAround(event.getBlockPos(), mc.level)) {
            event.setCanceled(true);
            renderEndSky(event);
        }
    }

    @SubscribeEvent
    public void onHeadInteract(PlayerInteractEvent.EntityInteract event){
        if (event.getTarget() instanceof PlayerHeadEntity){
            PlayerHeadEntity head = (PlayerHeadEntity) event.getTarget();
            if (head.getOwner() == event.getPlayer()){
                ClientUtil.setCameraEntityPreventShaderSwitch(mc.player);
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        EntityModel model = event.getRenderer().getModel();
        LivingEntity entity = event.getEntity();
        if (model instanceof BipedModel && entity instanceof AbstractClientPlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Optional<EntityZipperCapability> capability = player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve();
            if (capability.isPresent()){
                if (capability.get().noArms() && capability.get().noLegs()){
                    ((BipedModel<?>) model).head.setPos(0.0F, 0.0F, -2.5F);
                    ((BipedModel<?>) model).body.setPos(0.0F, 0.0F, -2.5F);
                }
                ((BipedModel<?>) model).head.visible = capability.get().hasHead();
                ((BipedModel<?>) model).leftArm.visible = !capability.get().isLeftArmBlocked();
                ((BipedModel<?>) model).rightArm.visible = !capability.get().isRightArmBlocked();
                ((BipedModel<?>) model).leftLeg.visible = !capability.get().isLeftLegBlocked();
                ((BipedModel<?>) model).rightLeg.visible = !capability.get().isRightLegBlocked();
                if (model instanceof PlayerModel){
                    ((PlayerModel<?>) model).hat.visible = capability.get().hasHead();
                    ((PlayerModel<?>) model).leftSleeve.visible = !capability.get().isLeftArmBlocked();
                    ((PlayerModel<?>) model).rightSleeve.visible = !capability.get().isRightArmBlocked();
                    ((PlayerModel<?>) model).leftPants.visible = !capability.get().isLeftLegBlocked();
                    ((PlayerModel<?>) model).rightPants.visible = !capability.get().isRightLegBlocked();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void addToScreen(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (screen instanceof ChatScreen) {
            Entity cameraEntity = mc.cameraEntity;
            if (cameraEntity != null && cameraEntity.getType() == InitEntities.PLAYER_HEAD.get()) {
                int x = screen.width / 2 - 100;
                int y = screen.height - 40;
                Button angeloRockDieButton = new Button(x, y, 200, 20,
                        new TranslationTextComponent(mc.level.getLevelData().isHardcore() ? "deathScreen.spectate" : "deathScreen.respawn"),
                        button -> AddonPackets.sendToServer(new HeadRespawnPacket()));
                event.addWidget(angeloRockDieButton);
            }
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
