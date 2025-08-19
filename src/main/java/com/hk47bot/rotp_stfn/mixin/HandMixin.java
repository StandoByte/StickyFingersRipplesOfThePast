package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.util.mc.MCUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MCUtil.class, remap = false)
public class HandMixin {
    @Inject(method = "areHandsFree", at = @At("RETURN"), cancellable = true)
    private static void checkIfArmIsUnzipped(LivingEntity entity, Hand[] hands, CallbackInfoReturnable<Boolean> cir){
        entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(zipperCap -> {
            for (Hand hand : hands){
                cir.setReturnValue(!zipperCap.isHandBlocked(hand));
            }
        });
    }
}
