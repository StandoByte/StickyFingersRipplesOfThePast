package com.hk47bot.rotp_stfn.effect;

import com.hk47bot.rotp_stfn.init.InitEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZipperEffect extends Effect {
    public ZipperEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityHurt(LivingHurtEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        if (!livingEntity.level.isClientSide()){
            if (livingEntity.hasEffect(InitEffects.ZIPPER.get())){
                event.setAmount(event.getAmount() * 2);
            }
        }
    }

}
