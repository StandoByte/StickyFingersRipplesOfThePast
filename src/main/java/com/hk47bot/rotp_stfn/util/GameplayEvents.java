package com.hk47bot.rotp_stfn.util;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCap;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCapProvider;
import com.hk47bot.rotp_stfn.init.InitEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GameplayEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        event.world.getCapability(ZipperStorageCapProvider.CAPABILITY).ifPresent(ZipperStorageCap::tick);
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity.getEffect(InitEffects.ZIP_WOUNDS.get()) != null && event.getAmount() > 0) {
            entity.removeEffect(InitEffects.ZIP_WOUNDS.get());
        }
    }

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (!entity.level.isClientSide()){
            entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(EntityZipperCapability::tickArms);
        }
        entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(EntityZipperCapability::tickLegs);
    }
}
