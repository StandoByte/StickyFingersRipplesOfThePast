package com.hk47bot.rotp_stfn.capability;


import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation CAPABILITY_ID = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_storage_data");
    
    // Register our capability (this method is called from another event handler, which uses a different mod bus).
    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(ZipperStorageCap.class, new ZipperStorageCapStorage(), () -> new ZipperStorageCap(null));
    }

    // Attaches the capability to all instances of LivingEntity.
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        event.addCapability(CAPABILITY_ID, new ZipperStorageCapProvider(world));
    }


    // Event handlers to properly sync the attached data from server to client(s).
    @SubscribeEvent
    public static void syncWithNewPlayer(PlayerEvent.StartTracking event) {
        Entity entityTracked = event.getTarget();
        ServerPlayerEntity trackingPlayer = (ServerPlayerEntity) event.getPlayer();
        if (entityTracked instanceof LivingEntity) {

        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    private static void syncAttachedData(PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

    }
}
