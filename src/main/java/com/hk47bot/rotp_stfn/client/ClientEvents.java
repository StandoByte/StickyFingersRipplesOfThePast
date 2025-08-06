package com.hk47bot.rotp_stfn.client;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.mrpresident.CocoJumboTurtleEntity;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.client.render.renderer.tileentity.StickyFingersZipperBlockRenderer;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import com.hk47bot.rotp_stfn.init.InitEntities;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.HeadRespawnPacket;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null || mc.level == null) return;

        if (ZipperUtil.hasZippersAround(mc.player.blockPosition(), mc.level)) {
            mc.options.setCameraType(PointOfView.FIRST_PERSON);
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
        if (model instanceof BipedModel) {
            Optional<EntityZipperCapability> capability = entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve();
            if (capability.isPresent()){
                if (capability.get().noLegs() && !(entity instanceof PlayerEntity)){
                    event.getMatrixStack().translate(0.0D, -1.0D, 0.3D);
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

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    @SubscribeEvent(priority = EventPriority.LOW)
    public void renderCarriedBodyPartSlot(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && !mc.player.isSpectator()) {
            for (Entity passenger : mc.player.getPassengers()) {
                if (passenger instanceof BodyPartEntity && BodyPartEntity.isCarriedTurtle(passenger, mc.player) && !CocoJumboTurtleEntity.isCarriedTurtle(passenger, mc.player)) {
                    LivingEntity entityToRender = (LivingEntity) passenger;

                    MatrixStack matrixStack = event.getMatrixStack();
                    HandSide offHand = mc.player.getMainArm().getOpposite();
                    int screenHeight = event.getWindow().getGuiScaledHeight();
                    int halfWidth = event.getWindow().getGuiScaledWidth() / 2;
                    IngameGui gui = mc.gui;
                    int blitOffs = gui.getBlitOffset();

                    mc.getTextureManager().bind(WIDGETS_LOCATION);
                    gui.setBlitOffset(-90);
                    if (offHand == HandSide.LEFT) {
                        gui.blit(matrixStack, halfWidth - 91 - 29, screenHeight - 23, 24, 22, 29, 24);
                    } else {
                        gui.blit(matrixStack, halfWidth + 91, screenHeight - 23, 53, 22, 29, 24);
                    }
                    gui.setBlitOffset(blitOffs);

                    int slotX = offHand == HandSide.LEFT ? halfWidth - 91 - 14 : halfWidth + 91 + 15;
                    int slotY = screenHeight - 9;
                    float entityScale = 12.0F;

                    renderEntityInGui(slotX - 3, slotY -1, (int) entityScale, entityToRender);
                    break;
                }
            }
        }
    }

    public static void renderEntityInGui(int x, int y, int scale, LivingEntity entity) {
        float yaw = -120.0F;
        float pitch = 30.0F;

        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);

        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        matrixstack.mulPose(quaternion);

        float originalBodyYaw = entity.yBodyRot;
        float originalYaw = entity.yRot;
        float originalPitch = entity.xRot;
        float originalPrevHeadYaw = entity.yHeadRotO;
        float originalHeadYaw = entity.yHeadRot;

        entity.yBodyRot = yaw;
        entity.yRot = yaw;
        entity.xRot = pitch;
        entity.yHeadRot = yaw;
        entity.yHeadRotO = yaw;

        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderermanager.overrideCameraOrientation(new Quaternion(0, 0, 0, 1));
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        });

        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);

        entity.yBodyRot = originalBodyYaw;
        entity.yRot = originalYaw;
        entity.xRot = originalPitch;
        entity.yHeadRotO = originalPrevHeadYaw;
        entity.yHeadRot = originalHeadYaw;

        RenderSystem.popMatrix();
    }
}