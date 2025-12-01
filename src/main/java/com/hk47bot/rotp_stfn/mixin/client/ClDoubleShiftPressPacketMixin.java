package com.hk47bot.rotp_stfn.mixin.client;

import com.github.standobyte.jojo.network.packets.fromclient.ClDoubleShiftPressPacket;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClDoubleShiftPressPacket.Handler.class, remap = false)
public class ClDoubleShiftPressPacketMixin {
    @Inject(method = "sendOnPress", at = @At("RETURN"), cancellable = true)
    private static void doubleShiftForZipperWalk(PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
        if (player.isAlive()
                && player.isOnGround()
                && IStandPower.getPlayerStandPower(player).getType() == InitStands.STAND_STICKY_FINGERS.getStandType()
                && IStandPower.getPlayerStandPower(player).getResolveLevel() == IStandPower.getPlayerStandPower(player).getMaxResolveLevel()){
            cir.setReturnValue(true);
        }
    }
}
