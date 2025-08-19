package com.hk47bot.rotp_stfn.entity.projectile;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.init.InitEntities;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.OwnerBoundProjectileEntity;

import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.UUID;

public class ExtendedPunchEntity extends OwnerBoundProjectileEntity {

    @Setter
    public boolean bindEntities;
    private static final UUID MANUAL_MOVEMENT_LOCK = UUID.fromString("3f243248-974a-41f6-940e-13554f3dc2fc");
    private StandEntity stand;
    private boolean caughtAnEntity = false;

    public ExtendedPunchEntity(World world, StandEntity entity, IStandPower power) {
        super(InitEntities.EXTENDED_PUNCH.get(), entity, world);
        stand = entity;
        
    }
    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!level.isClientSide() && stand != null && caughtAnEntity) {
            stand.getManualMovementLocks().removeLock(MANUAL_MOVEMENT_LOCK);
        }
    }
    @Override
    protected void afterBlockHit(BlockRayTraceResult blockRayTraceResult, boolean blockDestroyed) {
        setIsRetracting(true);
    }

    @Override
    protected boolean hurtTarget(Entity target, LivingEntity owner) {
        if (getEntityAttachedTo() == null && bindEntities) {
            if (target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                if (!JojoModUtil.isTargetBlocking(livingTarget)) {
                    attachToEntity(livingTarget);
                    playSound(ModSounds.HIEROPHANT_GREEN_GRAPPLE_CATCH.get(), 1.0F, 1.0F);
                    caughtAnEntity = true;
                }
            }
        }
        return super.hurtTarget(target, owner);
    }


    public ExtendedPunchEntity(EntityType<? extends ExtendedPunchEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!isAlive()) {
            return;
        }
        LivingEntity bound = getEntityAttachedTo();
        if (bound != null) {
            LivingEntity owner = getOwner();
            if (!bound.isAlive()) {
                if (!level.isClientSide()) {
                    remove();
                }
            }
            else if (owner != null) {
                Vector3d vecToOwner = owner.position().subtract(bound.position());
                double length = vecToOwner.length();
                if (length < 2) {
                    if (!level.isClientSide()) {
                        remove();
                    }
                }
                else {
                    dragTarget(bound, vecToOwner.normalize().scale(1));
                    bound.fallDistance = 0;
                }
            }
        }
    }



    @Override
    public boolean standDamage() {
        return true;
    }

    @Override
    public float getBaseDamage() {
        return 2.5F;
    }

    @Override
    protected float getMaxHardnessBreakable() {
        return 5.0F;
    }

    @Override
    public int ticksLifespan() {
        return InitStands.STICKY_FINGERS_EXTENDED_PUNCH.get().getStandActionTicks(null, null);
    }
    @Override
    protected float movementSpeed() {
        return 0.3F;
    }

    @Override
    protected int timeAtFullLength() {
        return 1;
    }

    @Override
    protected float retractSpeed() {
        return movementSpeed() * 5F;
    }

    @Override
    public boolean isBodyPart() {
        return true;
    }

    private static final Vector3d OFFSET = new Vector3d(-0.35, -0.2, 0.1);
    @Override
    protected Vector3d getOwnerRelativeOffset() {
        return OFFSET;
    }
}
