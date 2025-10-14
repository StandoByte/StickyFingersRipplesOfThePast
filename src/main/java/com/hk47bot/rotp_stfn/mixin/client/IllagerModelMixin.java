package com.hk47bot.rotp_stfn.mixin.client;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerModel.class)
public class IllagerModelMixin {
    @Shadow
    public ModelRenderer arms;

    @Shadow
    public ModelRenderer leftArm;

    @Shadow
    public ModelRenderer rightArm;

    @Inject(method = "setupAnim(Lnet/minecraft/entity/monster/AbstractIllagerEntity;FFFFF)V", at = @At(value = "TAIL"))
    private void unzippedArmsFix(AbstractIllagerEntity illager, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_, CallbackInfo ci){
        illager.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(zipperCap -> {
            AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = illager.getArmPose();
            boolean flag = abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSED;
            this.arms.visible = flag && !zipperCap.noArms();
            this.leftArm.visible = !flag && !zipperCap.isLeftArmBlocked();
            this.rightArm.visible = !flag && !zipperCap.isRightArmBlocked();
        });
    }
}
