package com.hk47bot.rotp_stfn.entity.bodypart;

import com.github.standobyte.jojo.util.mc.EntityOwnerResolver;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BodyPartEntity extends CreatureEntity implements IEntityAdditionalSpawnData, IFlyingAnimal {
    public EntityOwnerResolver owner = new EntityOwnerResolver();
    private boolean isReturning = false;
    protected GoToOwnerGoal goToOwnerGoal;

    public BodyPartEntity(EntityType<? extends BodyPartEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.moveControl = new FlyingMovementController(this, 70, true);
        this.navigation = new FlyingPathNavigator(this, p_i48580_2_);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
    }

    @Override
    protected void registerGoals() {
        goToOwnerGoal = new GoToOwnerGoal(this, 1D);
        this.goalSelector.addGoal(0, goToOwnerGoal);
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, world);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(true); // idk for what this
        flyingpathnavigator.setCanPassDoors(true); // no
        return flyingpathnavigator;
    }

    public static AttributeModifierMap.MutableAttribute createBodyPartAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, 3F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public void setOwner(LivingEntity entity) {
        owner.setOwner(entity);
    }

    @Override
    public float getWalkTargetValue(BlockPos p_205022_1_, IWorldReader p_205022_2_) {
        return p_205022_2_.getBlockState(p_205022_1_).isAir() ? 10.0F : 5.0F;
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        owner.loadNbt(nbt, "OwnerId");
        this.isReturning = nbt.getBoolean("IsReturning");
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        owner.saveNbt(nbt, "OwnerId");
        nbt.putBoolean("IsReturning", this.isReturning);
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (this.getOwner() == null || !this.getOwner().isAlive()) {
                this.remove();
                return;
            }

            if (isReturning) {
                goToOwnerGoal.start();
                LivingEntity ownerEntity = getOwner();
                if (this.distanceToSqr(ownerEntity.position()) <= 2) {
                    ownerEntity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                        if (this instanceof PlayerArmEntity) {
                            if (((PlayerArmEntity) this).isRight()) cap.setRightArmBlocked(false);
                            else cap.setLeftArmBlocked(false);
                        } else if (this instanceof PlayerLegEntity) {
                            if (((PlayerLegEntity) this).isRight()) cap.setRightLegBlocked(false);
                            else cap.setLeftLegBlocked(false);
                        } else if (this instanceof PlayerHeadEntity) {
                            cap.setHead(true);
                        }
                    });
                    this.remove();
                }
            } else {
                goToOwnerGoal.stop();
            }
        }
    }

    public void startReturning() {
        this.isReturning = true;
    }

    @Nullable
    public LivingEntity getOwner() {
        return owner.getEntityLiving(this.level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.isFire() || source.isExplosion() || source == DamageSource.CACTUS) {
            return super.hurt(source, amount);
        }

        LivingEntity ownerEntity = getOwner();
        if (ownerEntity != null && !this.level.isClientSide) {
            return ownerEntity.hurt(source, amount);
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public float getHealth() {
        LivingEntity ownerEntity = getOwner();
        return ownerEntity != null ? ownerEntity.getHealth() : super.getHealth();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        owner.writeNetwork(buffer);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        owner.readNetwork(additionalData);
//        if (this instanceof PlayerHeadEntity && this.getOwner() == ClientUtil.getClientPlayer()) {
//            ClientUtil.setCameraEntityPreventShaderSwitch(this);
//        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    static class GoToOwnerGoal extends Goal {
        private final BodyPartEntity bodyPart;
        private final double speedModifier;
        private LivingEntity owner;

        public GoToOwnerGoal(BodyPartEntity bodyPart, double speed) {
            this.bodyPart = bodyPart;
            this.speedModifier = speed;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.bodyPart.isReturning) {
                return false;
            }
            this.owner = this.bodyPart.getOwner();
            if (this.owner == null) {
                return false;
            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return this.bodyPart.isReturning && this.owner != null && this.owner.isAlive();
        }

        @Override
        public void start() {
            if (this.owner != null) {
                this.bodyPart.getNavigation().moveTo(this.bodyPart.getNavigation().createPath(this.owner, 0), this.speedModifier);
            }
        }

        @Override
        public void stop() {
            this.owner = null;
            this.bodyPart.getNavigation().stop();
        }
    }
}