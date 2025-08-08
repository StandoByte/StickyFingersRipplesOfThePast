package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SSetSlotPacket;
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
        CPlayerDiggingPacket.Action action = packet.getAction();

        if (action == CPlayerDiggingPacket.Action.SWAP_ITEM_WITH_OFFHAND ||
                action == CPlayerDiggingPacket.Action.DROP_ITEM ||
                action == CPlayerDiggingPacket.Action.DROP_ALL_ITEMS) {

            Entity carriedPart = null;
            for (Entity passenger : player.getPassengers()) {
                if (BodyPartEntity.isCarriedTurtle(passenger, player)) {
                    carriedPart = passenger;
                    break;
                }
            }

            if (carriedPart != null) {
                ci.cancel();

                carriedPart.stopRiding();

                if (action == CPlayerDiggingPacket.Action.DROP_ITEM ||
                        action == CPlayerDiggingPacket.Action.DROP_ALL_ITEMS) {
                    carriedPart.setDeltaMovement(player.getLookAngle().scale(0.5));
                }

                player.connection.send(new SSetSlotPacket(0, 36 + player.inventory.selected, player.getMainHandItem())); // костыли наше всё
                player.connection.send(new SSetSlotPacket(0, 45, player.getOffhandItem()));
            }
        }
    }
}