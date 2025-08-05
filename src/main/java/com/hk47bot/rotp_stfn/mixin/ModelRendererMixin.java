package com.hk47bot.rotp_stfn.mixin;

import net.minecraft.client.renderer.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelRenderer.class)
public class ModelRendererMixin {
    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyVisibility(ModelRenderer renderer, CallbackInfo ci){

    }
}
