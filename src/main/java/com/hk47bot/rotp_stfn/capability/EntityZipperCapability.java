package com.hk47bot.rotp_stfn.capability;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedArmEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedHeadEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedLegEntity;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.EntityZipperCapSyncPacket;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class EntityZipperCapability {
    private final LivingEntity entity;

    @Getter
    private boolean isInGround = false;
    @Setter
    private EntityZipperStorage storage = null;

    @Getter
    private boolean hasHead = true;
    @Getter
    private boolean leftArmBlocked = false;
    @Getter
    private boolean rightArmBlocked = false;
    @Getter
    private boolean rightLegBlocked = false;
    @Getter
    private boolean leftLegBlocked = false;

    @Getter @Setter
    private boolean shouldAddHead = false;
    @Getter @Setter
    private boolean shouldAddleftArm = false;
    @Getter @Setter
    private boolean shouldAddrightArm = false;
    @Getter @Setter
    private boolean shouldAddrightLeg = false;
    @Getter @Setter
    private boolean shouldAddleftLeg = false;

    @Getter
    private UUID headId = null;
    @Getter
    private UUID leftArmId = null;
    @Getter
    private UUID rightArmId = null;
    @Getter
    private UUID rightLegId = null;
    @Getter
    private UUID leftLegId = null;

    @Getter
    private boolean wallClimbing = false;

    @Getter
    private BlockPos climbStartPos = BlockPos.ZERO;

    public EntityZipperCapability(LivingEntity entity) {
        this.entity = entity;
    }

    public int getEntityId() {
        return entity.getId();
    }

    public void setHead(boolean hasHead) {
        this.hasHead = hasHead;
        if (entity != null) {
            if (hasHead && entity.level.isClientSide()) shouldAddHead = true;
            syncData(entity);
        }
    }

    public void setLeftArmBlocked(boolean leftArmBlocked) {
        this.leftArmBlocked = leftArmBlocked;
        if (entity != null) {
            if (!leftArmBlocked && entity.level.isClientSide()) shouldAddleftArm = true;
            syncData(entity);
        }
    }

    public void setRightArmBlocked(boolean rightArmBlocked) {
        this.rightArmBlocked = rightArmBlocked;
        if (entity != null) {
            if (!rightArmBlocked && entity.level.isClientSide()) shouldAddrightArm = true;
            syncData(entity);
        }
    }

    public void setRightLegBlocked(boolean rightLegBlocked) {
        this.rightLegBlocked = rightLegBlocked;
        if (entity != null) {
            if (!rightLegBlocked && entity.level.isClientSide()) shouldAddrightLeg = true;
            syncData(entity);
        }
    }

    public void setLeftLegBlocked(boolean leftLegBlocked) {
        this.leftLegBlocked = leftLegBlocked;
        if (entity != null) {
            if (!leftLegBlocked && entity.level.isClientSide()) shouldAddleftLeg = true;
            syncData(entity);
        }
    }

    public void setWallClimbing(boolean wallClimbing){
        this.wallClimbing = wallClimbing;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setClimbStartPos(BlockPos climbStartPos){
        this.climbStartPos = climbStartPos;
        if (entity != null) {
            syncData(entity);
        }
    }

    public EntityZipperStorage getOrCreateZipperStorage(Entity target){
        if (storage == null){
            this.setStorage(new EntityZipperStorage(target.getDisplayName(), target.getUUID()));
        }
        return storage;
    }

    public boolean noLegs() {
        return isLeftLegBlocked() && isRightLegBlocked();
    }

    public boolean noArms() {
        return isLeftArmBlocked() && isRightArmBlocked();
    }

    public void syncData(LivingEntity entity) {
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new EntityZipperCapSyncPacket(this), entity);
            setStorage(getOrCreateZipperStorage(entity));
        }
    }

    public void tick(){
        if (!entity.level.isClientSide()){
            tickArms();
            if (!entity.isAlive() && storage != null){
                InventoryHelper.dropContents(entity.level, entity, storage);
            }
        }
        wallClimbing = IStandPower.getStandPowerOptional(entity).isPresent() && IStandPower.getStandPowerOptional(entity).map(stand -> stand.getType() == InitStands.STAND_STICKY_FINGERS.getStandType()).get() && wallClimbing;
        tickLegs();
        tickInGround();
        if (noArms() && noLegs() && !(entity instanceof PlayerEntity)){
            entity.addEffect(new EffectInstance(ModStatusEffects.STUN.get(), 10, 10, false, false, false));
        }
    }

    public void tickLegs() {
        if (noLegs()) {
            entity.setSwimming(true);
            if (!(entity instanceof PlayerEntity)) {
                entity.setPose(Pose.SWIMMING);
            }
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 2, 10, false, false, false));
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y > 0 ? entity.getDeltaMovement().y / 2 : entity.getDeltaMovement().y, entity.getDeltaMovement().z);
            if (noArms()) {
                entity.setDeltaMovement(0, entity.hasEffect(Effects.LEVITATION) ? (0.05D * (double) (entity.getEffect(Effects.LEVITATION).getAmplifier() + 1) - entity.getDeltaMovement().y) * 0.2D : entity.getDeltaMovement().y, 0);
            }
        } else if (isLeftLegBlocked() || isRightLegBlocked()) {
            entity.setSwimming(false);
            if (!(entity instanceof PlayerEntity) && entity.getPose() == Pose.SWIMMING) {
                entity.setPose(Pose.STANDING);
            }
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.abilities.flying = false;
            }
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 10, 10, false, false, false));

            checkBodyPartEntity(leftLegId, () -> setLeftLegId(null));
            checkBodyPartEntity(rightLegId, () -> setRightLegId(null));
        }
    }

    public boolean isHandBlocked(Hand hand){
        boolean isRightHanded = (entity.getMainArm() == HandSide.RIGHT) == (hand == Hand.MAIN_HAND);
        return isRightHanded ? isRightArmBlocked() : isLeftArmBlocked();
    }


    public void tickArms() {
        if (isLeftArmBlocked()) {
            ItemStack handItem = entity.getItemInHand(entity.getMainArm() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND);
            if (!handItem.isEmpty()) {
                ItemStack droppedItem = handItem.copy();
                entity.setItemInHand(entity.getMainArm() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND, ItemStack.EMPTY);
                ItemEntity itemEntity = new ItemEntity(
                        entity.level,
                        entity.getX(),
                        entity.getY() + entity.getEyeHeight(),
                        entity.getZ(),
                        droppedItem
                );
                itemEntity.setPickUpDelay(40);
                entity.level.addFreshEntity(itemEntity);
            }
            checkBodyPartEntity(leftArmId, () -> setLeftArmId(null));
        }

        if (isRightArmBlocked()) {
            ItemStack handItem = entity.getItemInHand(entity.getMainArm() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND);
            if (!handItem.isEmpty()) {
                ItemStack droppedItem = handItem.copy();
                entity.setItemInHand(entity.getMainArm() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND, ItemStack.EMPTY);
                ItemEntity itemEntity = new ItemEntity(
                        entity.level,
                        entity.getX(),
                        entity.getY() + entity.getEyeHeight(),
                        entity.getZ(),
                        droppedItem
                );
                itemEntity.setPickUpDelay(40);
                entity.level.addFreshEntity(itemEntity);
            }

            checkBodyPartEntity(rightArmId, () -> setRightArmId(null));
        }
    }

    public void tickInGround(){
        IStandPower.getStandPowerOptional(entity).ifPresent(power -> {
            if (power.getType() == InitStands.STAND_STICKY_FINGERS.getStandType()) {
                if (isInGround) {
                    entity.addEffect(new EffectInstance(ModStatusEffects.FULL_INVISIBILITY.get(), 10, 0, false, false, false));
                    power.consumeStamina(5F, true);
                }
                if (ZipperUtil.hasZippersAround(entity.blockPosition(), entity.level)) {
                    setInGround(true);
                } else if (ZipperUtil.isBlockFree(entity.level, entity.blockPosition().above())
                        || power.getStamina() <= 0
                        || ZipperUtil.isBlockZipper(entity.level, entity.blockPosition())) {
                    setInGround(false);
                }

            }
        });
    }

    private void checkBodyPartEntity(UUID id, Runnable trouble) {
        if (id == null || entity.level.isClientSide()) {
            return;
        }

        ServerWorld serverLevel = (ServerWorld) entity.level;
        if (serverLevel.getEntity(id) == null) {
            trouble.run();
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putBoolean("InGround", isInGround);

        nbt.putBoolean("IsClimbingZipper", wallClimbing);

        if (storage != null) nbt.put("ZipperStorage", storage.createTag());

        nbt.putBoolean("RightArm", rightArmBlocked);
        nbt.putBoolean("LeftArm", leftArmBlocked);
        nbt.putBoolean("RightLeg", rightLegBlocked);
        nbt.putBoolean("LeftLeg", leftLegBlocked);
        nbt.putBoolean("HasHead", hasHead);

        if (rightArmId != null) nbt.putUUID("RightArmId", rightArmId);
        if (leftArmId != null) nbt.putUUID("LeftArmId", leftArmId);
        if (rightLegId != null) nbt.putUUID("RightLegId", rightLegId);
        if (leftLegId != null) nbt.putUUID("LeftLegId", leftLegId);
        if (headId != null) nbt.putUUID("HeadId", headId);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {

        isInGround = nbt.getBoolean("InGround");

        wallClimbing = nbt.getBoolean("IsClimbingZipper");

        if (nbt.contains("ZipperStorage")) getOrCreateZipperStorage(entity).fromTag((ListNBT) nbt.get("ZipperStorage"));

        rightArmBlocked = nbt.getBoolean("RightArm");
        leftArmBlocked = nbt.getBoolean("LeftArm");
        rightLegBlocked = nbt.getBoolean("RightLeg");
        leftLegBlocked = nbt.getBoolean("LeftLeg");
        hasHead = nbt.getBoolean("HasHead");

        rightArmId = nbt.hasUUID("RightArmId") ? nbt.getUUID("RightArmId") : null;
        leftArmId = nbt.hasUUID("LeftArmId") ? nbt.getUUID("LeftArmId") : null;
        rightLegId = nbt.hasUUID("RightLegId") ? nbt.getUUID("RightLegId") : null;
        leftLegId = nbt.hasUUID("LeftLegId") ? nbt.getUUID("LeftLegId") : null;
        headId = nbt.hasUUID("HeadId") ? nbt.getUUID("HeadId") : null;
    }



    // fuckin ids

    public void setHeadId(UUID headId) {
        this.headId = headId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setLeftArmId(UUID leftArmId) {
        this.leftArmId = leftArmId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setRightArmId(UUID rightArmId) {
        this.rightArmId = rightArmId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setRightLegId(UUID rightLegId) {
        this.rightLegId = rightLegId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setLeftLegId(UUID leftLegId) {
        this.leftLegId = leftLegId;
        if (entity != null) {
            syncData(entity);
        }
    }
    public void setInGround(boolean inGround) {
        this.isInGround = inGround;
        if (entity != null) {
            syncData(entity);
        }
    }
    public boolean isPartVisible(BodyPartEntity bodyPart) {
        if (bodyPart instanceof UnzippedHeadEntity){
            return isHasHead();
        }
        else if (bodyPart instanceof UnzippedArmEntity){
            return ((UnzippedArmEntity) bodyPart).isRight() ? !isRightArmBlocked() : !isLeftArmBlocked();
        }
        else {
            return ((UnzippedLegEntity) bodyPart).isRight() ? !isRightLegBlocked() : !isLeftLegBlocked();
        }
    }
}
