package com.hk47bot.rotp_stfn.mixin.client;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.client.renderer.entity.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ArmorStandArmorModel.class)
public class ArmorStandArmorModelMixin<T extends LivingEntity> {
    @Inject(method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void copyModelVisibility(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        BipedModel<? extends LivingEntity> model = (BipedModel<? extends LivingEntity>) (Object) this;
        Optional<EntityZipperCapability> capability = entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve();

        if (capability.isPresent()) {
            EntityZipperCapability cap = capability.get();

            model.head.visible = cap.hasHead();
            model.head.children.forEach(m -> m.visible = cap.hasHead());

            model.leftArm.visible = !cap.isLeftArmBlocked();
            model.leftArm.children.forEach(m -> m.visible = !cap.isLeftArmBlocked());

            model.rightArm.visible = !cap.isRightArmBlocked();
            model.rightArm.children.forEach(m -> m.visible = !cap.isRightArmBlocked());

            model.leftLeg.visible = !cap.isLeftLegBlocked();
            model.leftLeg.children.forEach(m -> m.visible = !cap.isLeftLegBlocked());

            model.rightLeg.visible = !cap.isRightLegBlocked();
            model.rightLeg.children.forEach(m -> m.visible = !cap.isRightLegBlocked());
        }
    }
}