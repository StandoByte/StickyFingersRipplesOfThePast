package com.hk47bot.rotp_stfn.client;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.client.render.renderer.StickyFingersZipperBlockRenderer;
import com.hk47bot.rotp_stfn.client.render.renderer.projectile.ExtendedPunchRenderer;

import com.hk47bot.rotp_stfn.client.render.renderer.stand.StickyFingersUpdatedRenderer;
import com.hk47bot.rotp_stfn.client.ui.ZipperInventoryMenu;
import com.hk47bot.rotp_stfn.client.ui.EntityZipperInventoryMenu;
import com.hk47bot.rotp_stfn.init.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(
                InitStands.STAND_STICKY_FINGERS.getEntityType(), StickyFingersUpdatedRenderer::new);

        ScreenManager.register(InitContainers.STICKY_FINGERS_ENTITY_CONTAINER.get(), EntityZipperInventoryMenu::new);
        ScreenManager.register(InitContainers.STICKY_FINGERS_BLOCK_CONTAINER.get(), ZipperInventoryMenu::new);

        RenderingRegistry.registerEntityRenderingHandler(InitEntities.EXTENDED_PUNCH.get(), ExtendedPunchRenderer::new);
        ClientRegistry.bindTileEntityRenderer(InitTileEntities.ZIPPER_TILE_ENTITY.get(), StickyFingersZipperBlockRenderer::new);
        RenderTypeLookup.setRenderLayer(InitBlocks.STICKY_FINGERS_ZIPPER.get(), RenderType.cutoutMipped());
    }
}
