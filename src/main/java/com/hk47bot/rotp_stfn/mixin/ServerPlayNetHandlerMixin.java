package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public abstract class ServerPlayNetHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    public void onItemSwapKey(CPlayerDiggingPacket packet, CallbackInfo ci) {
        if (packet.getAction() == CPlayerDiggingPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
            for (Entity passenger : player.getPassengers()) {
                if (BodyPartEntity.isCarriedTurtle(passenger, player)) {
                    passenger.stopRiding();
                    ci.cancel();
                }
            }
        }
    }
}