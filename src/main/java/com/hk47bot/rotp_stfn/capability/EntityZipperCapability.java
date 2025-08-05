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
    private int headId = -1;
    @Getter
    private int leftArmId = -1;
    @Getter
    private int rightArmId = -1;
    @Getter
    private int rightLegId = -1;
    @Getter
    private int leftLegId = -1;

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
            if (!(entity instanceof PlayerEntity)){
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
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("RightArm", rightArmBlocked);
        nbt.putBoolean("LeftArm", leftArmBlocked);
        nbt.putBoolean("RightLeg", rightLegBlocked);
        nbt.putBoolean("LeftLeg", leftLegBlocked);
        nbt.putBoolean("HasHead", hasHead);

        nbt.putInt("RightArmId", rightArmId);
        nbt.putInt("LeftArmId", leftArmId);
        nbt.putInt("RightLegId", rightLegId);
        nbt.putInt("LeftLegId", leftLegId);
        nbt.putInt("HeadId", headId);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        rightArmBlocked = nbt.getBoolean("RightArm");
        leftArmBlocked = nbt.getBoolean("LeftArm");
        rightLegBlocked = nbt.getBoolean("RightLeg");
        leftLegBlocked = nbt.getBoolean("LeftLeg");
        hasHead = nbt.getBoolean("HasHead");

        rightArmId = nbt.getInt("RightArmId");
        leftArmId = nbt.getInt("LeftArmId");
        rightLegId = nbt.getInt("RightLegId");
        leftLegId = nbt.getInt("LeftLegId");
        headId = nbt.getInt("HeadId");
    }

    public void setHead(boolean hasHead) {
        this.hasHead = hasHead;
        if (entity != null) {
            syncData(entity);
        }
    }

    // fuckin ids

    public void setHeadId(int headId) {
        this.headId = headId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setLeftArmId(int leftArmId) {
        this.leftArmId = leftArmId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setRightArmId(int rightArmId) {
        this.rightArmId = rightArmId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setRightLegId(int rightLegId) {
        this.rightLegId = rightLegId;
        if (entity != null) {
            syncData(entity);
        }
    }

    public void setLeftLegId(int leftLegId) {
        this.leftLegId = leftLegId;
        if (entity != null) {
            syncData(entity);
        }
    }
}
