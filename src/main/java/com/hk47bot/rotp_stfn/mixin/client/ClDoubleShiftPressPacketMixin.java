package com.hk47bot.rotp_stfn.mixin.client;

import com.github.standobyte.jojo.network.packets.fromclient.ClDoubleShiftPressPacket;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClDoubleShiftPressPacket.Handler.class, remap = false)
public class ClDoubleShiftPressPacketMixin {
    @Inject(method = "sendOnPress", at = @At("RETURN"), cancellable = true)
    private static void nuStandooooooo(PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(player.isAlive() && player.isOnGround());
    }
}
