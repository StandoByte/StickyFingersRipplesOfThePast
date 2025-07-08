package com.hk47bot.rotp_stfn.util;

import com.hk47bot.rotp_stfn.capability.ZipperStorageCap;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCapProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GameplayEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        event.world.getCapability(ZipperStorageCapProvider.CAPABILITY).ifPresent(ZipperStorageCap::tick);
    }
}
