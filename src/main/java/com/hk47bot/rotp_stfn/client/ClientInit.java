package com.hk47bot.rotp_stfn.client;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.client.render.renderer.stand.StickyFingersRenderer;
import com.hk47bot.rotp_stfn.client.render.renderer.projectile.ExtendedPunchRenderer;

import com.hk47bot.rotp_stfn.init.InitBlocks;
import com.hk47bot.rotp_stfn.init.InitEntities;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(InitStands.STAND_STICKY_FINGERS.getEntityType(), StickyFingersRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(InitEntities.EXTENDED_PUNCH.get(), ExtendedPunchRenderer::new);

        RenderTypeLookup.setRenderLayer(InitBlocks.STICKY_FINGERS_ZIPPER.get(), RenderType.cutoutMipped());
    }
}
