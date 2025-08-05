package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.action.stand.CrazyDiamondHeal;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.github.standobyte.jojo.action.stand.CrazyDiamondHeal.addParticlesAround;

@Mixin(value = CrazyDiamondHeal.class, remap = false)
public abstract class CDHealMixin {
    @Inject(method = "healLivingEntity", at = @At("RETURN"))
    public void healLivingEntity(World world, LivingEntity entity, StandEntity standEntity, StandEntityTask task, CallbackInfoReturnable<Boolean> cir) {
        Optional<EntityZipperCapability> optCap = entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve();
        if (!optCap.isPresent()) return;
        EntityZipperCapability capability = optCap.get();

        handleBodyPartReturn(world, entity, capability.getHeadId(), PlayerHeadEntity.class);
        handleBodyPartReturn(world, entity, capability.getLeftArmId(), PlayerArmEntity.class);
        handleBodyPartReturn(world, entity, capability.getRightArmId(), PlayerArmEntity.class);
        handleBodyPartReturn(world, entity, capability.getLeftLegId(), PlayerLegEntity.class);
        handleBodyPartReturn(world, entity, capability.getRightLegId(), PlayerLegEntity.class);
    }

    @Unique
    private boolean handleBodyPartReturn(World world, LivingEntity owner, int partId, Class<? extends BodyPartEntity> clazz) {
        if (partId != -1) {
            Entity partEntity = world.getEntity(partId);
            if (clazz.isInstance(partEntity)) {
                BodyPartEntity bodyPart = (BodyPartEntity) partEntity;
                bodyPart.startReturning();
                addParticlesAround(partEntity);
                return true;
            }
        }
        return false;
    }
}