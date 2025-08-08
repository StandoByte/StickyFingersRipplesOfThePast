package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getEyePosition", at = @At("HEAD"), cancellable = true)
    private void getEyePosition(float partialTicks, CallbackInfoReturnable<Vector3d> cir){
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity) {
            EntityZipperCapability capability = entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (capability == null) return;

            if (!capability.isHasHead() && capability.getHeadId() != -1) {
                Entity head = entity.level.getEntity(capability.getHeadId());
                if (head instanceof PlayerHeadEntity) {
                    cir.setReturnValue(head.getEyePosition(partialTicks));
                }
            }
        }
    }
}
