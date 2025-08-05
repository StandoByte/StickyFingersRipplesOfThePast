package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BipedModel.class)
public class BipedModelMixin<T extends LivingEntity> {

    @Inject(method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void copyModelVisibility(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        BipedModel<? extends Entity> model = (BipedModel<? extends Entity>) (Object) this;
        Optional<EntityZipperCapability> capability = entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve();
        model.head.visible = capability.get().hasHead();
        model.leftArm.visible = !capability.get().isLeftArmBlocked();
        model.rightArm.visible = !capability.get().isRightArmBlocked();
        model.leftLeg.visible = !capability.get().isLeftLegBlocked();
        model.rightLeg.visible = !capability.get().isRightLegBlocked();
    }
}
