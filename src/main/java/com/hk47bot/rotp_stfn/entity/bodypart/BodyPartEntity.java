package com.hk47bot.rotp_stfn.entity.bodypart;

import com.github.standobyte.jojo.entity.IPassengerMixinReposition;
import com.github.standobyte.jojo.util.general.MathUtil;
import com.github.standobyte.jojo.util.mc.EntityOwnerResolver;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.util.StickyUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class BodyPartEntity extends CreatureEntity implements IEntityAdditionalSpawnData, IFlyingAnimal, IPassengerMixinReposition {
    private static final DataParameter<Boolean> IS_RETURNING = EntityDataManager.defineId(BodyPartEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_CARRIED = EntityDataManager.defineId(BodyPartEntity.class, DataSerializers.BOOLEAN);
    public EntityOwnerResolver owner = new EntityOwnerResolver();

    @Getter
    @Setter
    private int lastTickNotified;
    protected GoToOwnerGoal goToOwnerGoal;

    private final PathNavigator groundPathNavigator = new GroundPathNavigator(this, this.level);
    private final MovementController groundMovementController = new MovementController(this);

    private final FlyingPathNavigator returnPathNavigator = new FlyingPathNavigator(this, this.level);
    private final FlyingMovementController returnMovementController = new FlyingMovementController(this, 70, true);

    public BodyPartEntity(EntityType<? extends BodyPartEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.moveControl = returnMovementController;
        this.navigation = returnPathNavigator;
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
    }

    @Override
    protected void registerGoals() {
        goToOwnerGoal = new GoToOwnerGoal(this, 1.5D);
        this.goalSelector.addGoal(0, goToOwnerGoal);
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, world);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(true);
        flyingpathnavigator.setCanPassDoors(true);
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
        this.setReturning(nbt.getBoolean("IsReturning"));
        entityData.set(IS_CARRIED, nbt.getBoolean("Carried"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        owner.saveNbt(nbt, "OwnerId");
        nbt.putBoolean("IsReturning", this.isReturning());
        nbt.putBoolean("Carried", isCarried());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_CARRIED, false);
        this.entityData.define(IS_RETURNING, false);
    }

    @Nonnull
    @Override
    public PathNavigator getNavigation() {
        if (isReturning()) {
            this.moveControl = returnMovementController;
            return returnPathNavigator;
        } else {
            this.moveControl = groundMovementController;
            return groundPathNavigator;
        }
    }

    @Override
    public void tick() {
        bandAidPreTick();
        super.tick();

        // FIXME: Пофиксить рассинхрон на клиенте между owner'ом (хз как)

        if (!this.level.isClientSide() && this.owner.getEntity(this.level) == null && this.tickCount % 40 == 0) {
            this.remove();
            return;
        }

        if (!this.level.isClientSide()) {
            if (isReturning()) {
                if (!(this.navigation instanceof FlyingPathNavigator)) {
                    this.navigation = returnPathNavigator;
                    this.navigation.stop();
                    this.moveControl = returnMovementController;
                    this.setNoGravity(true);
                }
            } else {
                if (!(this.navigation instanceof GroundPathNavigator)) {
                    this.navigation = groundPathNavigator;
                    this.navigation.stop();
                    this.moveControl = groundMovementController;
                    this.setJumping(false);
                    this.setNoGravity(false);
                }
            }

            if (this.lastTickNotified == -1) return;

            if (this.tickCount - this.lastTickNotified > 5) {
                setReturning(false);
                goToOwnerGoal.stop();
                this.lastTickNotified = -1;
                return;
            }

            if (isReturning()) {
                if (this.isCarried()) {
                    stopRiding();
                }

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
                this.setNoGravity(false);
            }
        }

        if (!level.isClientSide() && getVehicle() == null) {
            entityData.set(IS_CARRIED, false);
        }
        LivingEntity carrier = getCarrier();
        if (carrier != null) {
            boolean hasArmToPickup = carrier.getCapability(EntityZipperCapabilityProvider.CAPABILITY).map(cap -> {
                HandSide offHandSide = carrier.getMainArm().getOpposite();
                if (offHandSide == HandSide.LEFT) {
                    return !cap.isLeftArmBlocked();
                } else {
                    return !cap.isRightArmBlocked();
                }
            }).orElse(true);

            if (!MCUtil.itemHandFree(carrier.getItemInHand(Hand.OFF_HAND)) || carrier.isSpectator() || !hasArmToPickup) {
                stopRiding();
            } else {
                float headRotOffset = carrier.yBodyRot - this.yHeadRot;
                this.yRot = carrier.yBodyRot;
                this.yHeadRot += headRotOffset;
                this.yBodyRot += headRotOffset;
            }
        }
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand pHand) {
        ItemStack heldItem = player.getItemInHand(pHand);
        if (pHand == Hand.MAIN_HAND && !player.isShiftKeyDown() && !this.isPassenger() && !(heldItem.getItem() instanceof ShootableItem)) {

            boolean hasArmToPickup = player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).map(cap -> {
                HandSide offHandSide = player.getMainArm().getOpposite();
                if (offHandSide == HandSide.LEFT) {
                    return !cap.isLeftArmBlocked();
                } else {
                    return !cap.isRightArmBlocked();
                }
            }).orElse(true);

            if (!hasArmToPickup) {
                if (level.isClientSide()) {
                    player.displayClientMessage(new TranslationTextComponent("rotp_stfn.message.cant_pickup.no_arm"), true);
                }
                return ActionResultType.FAIL;
            }

            if (StickyUtil.isHandFree(player, Hand.OFF_HAND)) {
                if (this.startRiding(player, true)) {
                    if (!level.isClientSide()) {
                        entityData.set(IS_CARRIED, true);
                        IFormattableTextComponent keybind = new KeybindTextComponent("key.swapOffhand");
                        player.displayClientMessage(new TranslationTextComponent("body_part.hint.release", keybind, getDisplayName()), true);
                    }
                }
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            } else {
                if (level.isClientSide()) {
                    player.displayClientMessage(new TranslationTextComponent("body_part.carry.offhand", getDisplayName()), true);
                }
                return ActionResultType.PASS;
            }
        } else {
            return super.mobInteract(player, pHand);
        }
    }

    public void startReturning() {
        this.setReturning(true);
        this.lastTickNotified = this.tickCount;
    }

    public boolean isReturning() {
        return this.entityData.get(IS_RETURNING);
    }

    public void setReturning(boolean returning) {
        this.entityData.set(IS_RETURNING, returning);
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
        buffer.writeUtf(getOwner().getUUID().toString());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        UUID ownerUUID = UUID.fromString(additionalData.readUtf());
        RotpStickyFingersAddon.getLogger().info(MCUtil.getAllEntities(this.level));
        for (Entity entity : MCUtil.getAllEntities(this.level)){
            if (entity instanceof HuskEntity){
                RotpStickyFingersAddon.getLogger().info(ownerUUID);
                RotpStickyFingersAddon.getLogger().info(entity.getUUID());
            }
            if (entity instanceof LivingEntity && entity.getUUID().equals(ownerUUID)){
                this.setOwner((LivingEntity) entity);
                return;
            }
        }
        RotpStickyFingersAddon.getLogger().info("no one found");
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        RotpStickyFingersAddon.getLogger().info("packet sent");
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean isCarried() {
        return entityData.get(IS_CARRIED);
    }

    @Nullable
    public LivingEntity getCarrier() {
        if (isCarried()) {
            Entity vehicle = getVehicle();
            if (vehicle instanceof LivingEntity) {
                return ((LivingEntity) vehicle);
            }
        }

        return null;
    }

    @Override
    public void stopRiding() {
        if (cancelStopRiding()) {
            return;
        }
        super.stopRiding();
        if (!level.isClientSide() && getVehicle() == null) {
            entityData.set(IS_CARRIED, false);
        }
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> key) {
        super.onSyncedDataUpdated(key);
        if (IS_CARRIED.equals(key) && !entityData.get(IS_CARRIED)) {
            stopRiding();
        }
    }

    @Override
    public boolean isPickable() {
        return super.isPickable() && !isCarried();
    }

    @Override
    public boolean removeWhenFarAway(double distToClosestPlayer) {
        return false;
    }

    @Override
    public Vector3d repositionPassenger(Entity vehicle) {
        if (isCarried() && vehicle instanceof LivingEntity) {
            LivingEntity carrier = (LivingEntity) vehicle;
            Vector3d carryVec = carryOffset(carrier.yBodyRot, carrier);
            return vehicle.position().add(carryVec);
        }
        return null;
    }

    public static Vector3d carryOffset(float yRot, LivingEntity carrier) {
        HandSide offHand = MCUtil.getOppositeSide(carrier.getMainArm());
        float width = carrier.getBbWidth();
        Vector3d carryVec = new Vector3d(
                width * (offHand == HandSide.LEFT ? 0.55 : -0.55),
                carrier.getBbHeight() * 0.35,
                width * 0.75);

        carryVec = carryVec.yRot(-yRot * MathUtil.DEG_TO_RAD);

        return carryVec;
    }

    public static boolean isCarriedTurtle(Entity passenger, Entity carrier) {
        return passenger instanceof BodyPartEntity
                && ((BodyPartEntity) passenger).getCarrier() == carrier;
    }

    // the shit below prevents the turtle from dismounting when the player gets into water
    // (the actual logic for that in in LivingEntity#baseTick, and i ain't whipping out a mixin for that)
    private byte stopRidingIsDueToWater;

    private void bandAidPreTick() {
        stopRidingIsDueToWater = 0;
    }

    @Override
    public boolean isEyeInFluid(ITag<Fluid> tag) {
        if (stopRidingIsDueToWater == 0 && tag == FluidTags.WATER) {
            stopRidingIsDueToWater = 1;
        }
        return super.isEyeInFluid(tag);
    }

    @Override
    public boolean canBreatheUnderwater() {
        if (stopRidingIsDueToWater == 1) {
            stopRidingIsDueToWater = 2;
        }
        return super.canBreatheUnderwater();
    }

    @Override
    protected void onChangedBlock(BlockPos pos) {
        stopRidingIsDueToWater = -1;
        super.onChangedBlock(pos);
    }

    private boolean cancelStopRiding() {
        return stopRidingIsDueToWater == 2;
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
            if (!this.bodyPart.isReturning()) {
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
            return this.bodyPart.isReturning() && this.owner != null && this.owner.isAlive();
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
            this.bodyPart.setNoGravity(false);
        }
    }
}