package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.action.stand.StickyFingersClimbZipper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void rotp_sfPlayerWallClimb(Vector3d pTravelVector, CallbackInfo ci) {
        PlayerEntity thisPlayer = (PlayerEntity) (Object) this;
        if (StickyFingersClimbZipper.travelOnZipper(thisPlayer, pTravelVector)) {
            ci.cancel();
        }
    }
}
