package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {

    @Inject(method = "isControlledCamera", at = @At(value = "RETURN"), cancellable = true)
    private void isHeadControllingCamera(CallbackInfoReturnable<Boolean> cir){
        Minecraft minecraft = Minecraft.getInstance();
        cir.setReturnValue(minecraft.getCameraEntity() instanceof PlayerHeadEntity || minecraft.getCameraEntity() == (ClientPlayerEntity) (Object) this);
    }
}
