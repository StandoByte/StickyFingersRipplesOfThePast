package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StandType.class, remap = false)
public class StandTypeMixin {
    @Inject(method = "summon", at = @At(value = "HEAD"), cancellable = true)
    private void cancelItemStandSummonIfNoArms(LivingEntity user, IStandPower standPower, boolean withoutNameVoiceLine, CallbackInfoReturnable<Boolean> cir){
        standPower.getUser().getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(zipperCap -> {
            if (zipperCap.noArms() && !standPower.isActive() &&
                      (standPower.getType().getRegistryName().toString().contains("the_emperor")
                    || standPower.getType().getRegistryName().toString().contains("cream_starter"))){
                cir.setReturnValue(false);
            }
        });
    }
}
