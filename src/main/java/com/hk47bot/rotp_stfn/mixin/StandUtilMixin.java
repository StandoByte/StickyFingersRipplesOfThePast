package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = StandUtil.class)
public class StandUtilMixin {
    @Inject(method = "setManualControl", at = @At(value = "HEAD"), cancellable = true)
    private static void cancelControlWhenHead(PlayerEntity player, boolean manualControl, boolean keepPosition, CallbackInfo ci){
        player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(entityZipperCapability -> {
            if (!entityZipperCapability.isHasHead()){
                ci.cancel();
            }
        });
    }
}
