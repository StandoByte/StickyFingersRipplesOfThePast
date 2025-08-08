package com.hk47bot.rotp_stfn.capability;

import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.EntityZipperCapSyncPacket;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class EntityZipperCapability {
    private final LivingEntity entity;

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

    public EntityZipperCapability(LivingEntity entity) {
        this.entity = entity;
    }

    public int getEntityId() {
        return entity.getId();
    }

    public void setLeftArmBlocked(boolean leftArmBlocked) {
        this.leftArmBlocked = leftArmBlocked;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setRightArmBlocked(boolean rightArmBlocked) {
        this.rightArmBlocked = rightArmBlocked;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setRightLegBlocked(boolean rightLegBlocked) {
        this.rightLegBlocked = rightLegBlocked;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setLeftLegBlocked(boolean leftLegBlocked) {
        this.leftLegBlocked = leftLegBlocked;
        if (entity != null) {
            syncData(entity);
        }
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
        }
    }

    public void tickLegs() {
        if (noLegs()) {
            entity.setSwimming(true);
            if (!(entity instanceof PlayerEntity)) {
                entity.setPose(Pose.SWIMMING);
            }
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y > 0 ? entity.getDeltaMovement().y / 2 : entity.getDeltaMovement().y, entity.getDeltaMovement().z);
            if (noArms()) {
                entity.setDeltaMovement(0, entity.hasEffect(Effects.LEVITATION) ? (0.05D * (double) (entity.getEffect(Effects.LEVITATION).getAmplifier() + 1) - entity.getDeltaMovement().y) * 0.2D : entity.getDeltaMovement().y, 0);
            }
        } else if (isLeftLegBlocked() || isRightLegBlocked()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.abilities.flying = false;
            }
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 10, 10, false, false, false));

            checkBodyPartEntity(leftLegId, () -> setLeftLegId(null));
            checkBodyPartEntity(rightLegId, () -> setRightLegId(null));
        }
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

    public void setHead(boolean hasHead) {
        this.hasHead = hasHead;
        if (entity != null) {
            syncData(entity);
        }
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
}
