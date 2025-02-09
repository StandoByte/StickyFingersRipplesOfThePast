package com.hk47bot.rotp_stfn.entity.projectile;

import com.hk47bot.rotp_stfn.init.InitEntities;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.OwnerBoundProjectileEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class ExtendedPunchEntity extends OwnerBoundProjectileEntity {

    public ExtendedPunchEntity(World world, LivingEntity entity) {
        super(InitEntities.EXTENDED_PUNCH.get(), entity, world);
        
    }

    public ExtendedPunchEntity(EntityType<? extends ExtendedPunchEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean standDamage() {
        return true;
    }

    @Override
    public float getBaseDamage() {
        return 5.1429F;
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
        return 4;
    }

    @Override
    protected float retractSpeed() {
        return movementSpeed() * 3F;
    }

    @Override
    public boolean isBodyPart() {
        return true;
    }

    private static final Vector3d OFFSET = new Vector3d(-0.3, -0.2, 0.75);
    @Override
    protected Vector3d getOwnerRelativeOffset() {
        return OFFSET;
    }
}
