package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.entity.damaging.DamagingEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.OwnerBoundProjectileEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedHeadEntity;
import com.hk47bot.rotp_stfn.util.StickyUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = OwnerBoundProjectileEntity.class, remap = false)
public abstract class OwnerBoundProjectileEntityMixin extends DamagingEntity {

    public OwnerBoundProjectileEntityMixin(EntityType<? extends DamagingEntity> entityType, @Nullable LivingEntity owner, World world) {
        super(entityType, owner, world);
    }
    @Redirect(method = "ownerPosition", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/entity/damaging/projectile/ownerbound/OwnerBoundProjectileEntity;getPos(Lnet/minecraft/entity/LivingEntity;FFF)Lnet/minecraft/util/math/vector/Vector3d;"))
    private Vector3d getEyePosition(OwnerBoundProjectileEntity instance, LivingEntity owner, float partialTick, float yRot, float xRot){
        OwnerBoundProjectileEntity projectile = (OwnerBoundProjectileEntity) (Object) this;
        EntityZipperCapability capability = owner.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
        if (capability == null) return getPos(owner, partialTick, yRot, xRot);

        if (!capability.isHasHead() && capability.getHeadId() != null) {
            Entity head = StickyUtil.getEntityByUUID(owner.level, capability.getHeadId());
            if (head instanceof UnzippedHeadEntity && !(owner instanceof StandEntity)) {
                setRot(head.yRot, head.xRot);
                return head.getEyePosition(partialTick).add(getOwnerRelativeOffset());
            }
        }
        return getPos(owner, partialTick,
                projectile.isBodyPart() ? MathHelper.lerp(partialTick, owner.yBodyRotO, owner.yBodyRot) : MathHelper.lerp(partialTick, owner.yRotO, owner.yRot),
                MathHelper.lerp(partialTick, owner.xRotO, owner.xRot));
    }

    @Inject(method = "moveBoundToOwner", at = @At("RETURN"))
    private void cancelRotationIfIsBeheaded(CallbackInfoReturnable<Boolean> cir){
        Entity owner = getOwner();
        if (owner != null){
            EntityZipperCapability capability = owner.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (capability != null && !capability.isHasHead() && capability.getHeadId() != null) {
                Entity head = StickyUtil.getEntityByUUID(owner.level, capability.getHeadId());
                if (head instanceof UnzippedHeadEntity) {
                    setRot(head.yRot, head.xRot);
                }
            }
        }
    }
}
