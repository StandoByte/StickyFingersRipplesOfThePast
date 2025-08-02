package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "updateSwimming", at = @At(value = "TAIL"))
    private void IWANTTOSWIM(CallbackInfo ci){
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(capability -> {
                if (capability.noLegs()){
                    livingEntity.setSwimming(true);
                }
            });
        }
    }

}
