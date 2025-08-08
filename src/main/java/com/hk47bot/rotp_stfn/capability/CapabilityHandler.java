package com.hk47bot.rotp_stfn.capability;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID)
public class CapabilityHandler {
    private static final ResourceLocation ENTITY_CAP = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_entity_data");
    private static final ResourceLocation STORAGE_CAP = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_storage_data");

    public static void commonSetupRegister() {
        CapabilityManager.INSTANCE.register(ZipperStorageCap.class, new ZipperStorageCapStorage(), () -> new ZipperStorageCap(null));
        CapabilityManager.INSTANCE.register(EntityZipperCapability.class, new EntityZipperCapabilityStorage(), () -> new EntityZipperCapability(null));
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesToEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            event.addCapability(ENTITY_CAP, new EntityZipperCapabilityProvider((LivingEntity) entity));
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesToWorld(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        event.addCapability(STORAGE_CAP, new ZipperStorageCapProvider(world));
    }

    @SubscribeEvent
    public static void syncWithNewPlayer(PlayerEvent.StartTracking event) {
        Entity entityTracked = event.getTarget();
        if (entityTracked instanceof LivingEntity) {
            syncAttachedData((LivingEntity) entityTracked);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncAttachedData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(oldStore -> {
                event.getPlayer().getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(newStore -> {
                    newStore.fromNBT(oldStore.toNBT());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncAttachedData(event.getPlayer());
    }

    private static void syncAttachedData(LivingEntity entity) {
        entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(capability -> capability.syncData(entity));
    }
}