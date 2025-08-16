package com.hk47bot.rotp_stfn.mixin.client;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.hk47bot.rotp_stfn.util.ZipperUtil.hasZippersAround;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin extends AbstractClientPlayerEntity {

    public ClientPlayerMixin(ClientWorld p_i50991_1_, GameProfile p_i50991_2_) {
        super(p_i50991_1_, p_i50991_2_);
    }
    @Inject(method = "isControlledCamera", at = @At(value = "RETURN"), cancellable = true)
    private void isHeadControllingCamera(CallbackInfoReturnable<Boolean> cir){
        Minecraft minecraft = Minecraft.getInstance();
        cir.setReturnValue(minecraft.getCameraEntity() instanceof PlayerHeadEntity || minecraft.getCameraEntity() == this);
    }
    @Inject(method = "suffocatesAt(Lnet/minecraft/util/math/BlockPos;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void cancelSuffocationOnPos(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        this.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if (cap.isInGround()){
                cir.setReturnValue(false);
            }
        });
        if (hasZippersAround(pos, level)){
            cir.setReturnValue(false);
        }
    }
}
