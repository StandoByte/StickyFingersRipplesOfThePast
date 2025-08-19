package com.hk47bot.rotp_stfn.util;

import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.init.InitEffects;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GameplayEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWorldTick(TickEvent.WorldTickEvent event){
        event.world.getCapability(ZipperWorldCapProvider.CAPABILITY).ifPresent(ZipperWorldCap::tick);
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity.getEffect(InitEffects.ZIP_WOUNDS.get()) != null
                && event.getSource() instanceof StandEntityDamageSource
                && ((StandEntityDamageSource)event.getSource()).getStandPower().getType() == InitStands.STAND_STICKY_FINGERS.getStandType()
                && event.getAmount() > 0) {
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
        entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(EntityZipperCapability::tickInGround);
    }

    @SubscribeEvent
    public static void onSwimSizeSet(EntityEvent.Size event){
        Entity entity = event.getEntity();
        if (!(entity instanceof PlayerEntity) && entity instanceof LivingEntity && entity.getPose() == Pose.SWIMMING){
            event.setNewSize(new EntitySize(entity.getBbWidth(), entity.getBbWidth(), true));
            event.setNewEyeHeight(entity.getBbWidth() * 0.85F);
        }
    }
}
