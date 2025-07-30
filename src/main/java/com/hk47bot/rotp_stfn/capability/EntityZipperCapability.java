package com.hk47bot.rotp_stfn.capability;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.EntityZipperCapSyncPacket;
import com.hk47bot.rotp_stfn.network.ZipperStorageSyncPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;

public class EntityZipperCapability {
    private final LivingEntity entity;
    private boolean leftArmBlocked;
    private boolean rightArmBlocked;
    private boolean rightLegBlocked;
    private boolean leftLegBlocked;
    public EntityZipperCapability(LivingEntity entity) {
        this.entity = entity;
    }

    public int getEntityId(){
        return entity.getId();
    }

    public boolean isLeftArmBlocked() {
        return leftArmBlocked;
    }

    public void setLeftArmBlocked(boolean leftArmBlocked) {
        this.leftArmBlocked = leftArmBlocked;
        if (entity != null && entity instanceof ServerPlayerEntity){
            syncData((ServerPlayerEntity) entity);
        }
    }

    public boolean isRightArmBlocked() {
        return rightArmBlocked;
    }

    public void setRightArmBlocked(boolean rightArmBlocked) {
        this.rightArmBlocked = rightArmBlocked;
        if (entity != null && entity instanceof ServerPlayerEntity){
            syncData((ServerPlayerEntity) entity);
        }
    }

    public boolean isRightLegBlocked() {
        return rightLegBlocked;
    }

    public void setRightLegBlocked(boolean rightLegBlocked) {
        this.rightLegBlocked = rightLegBlocked;
        if (entity != null && entity instanceof ServerPlayerEntity){
            syncData((ServerPlayerEntity) entity);
        }
    }

    public boolean isLeftLegBlocked() {
        return leftLegBlocked;
    }

    public void setLeftLegBlocked(boolean leftLegBlocked) {
        this.leftLegBlocked = leftLegBlocked;
        if (entity != null && entity instanceof ServerPlayerEntity){
            syncData((ServerPlayerEntity) entity);
        }
    }

    public boolean noLegs(){
        return isLeftLegBlocked() && isRightLegBlocked();
    }

    public boolean noArms(){
        return isLeftArmBlocked() && isRightArmBlocked();
    }

    public void syncData(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            AddonPackets.sendToClient(new EntityZipperCapSyncPacket(this), (ServerPlayerEntity) player);
        }
    }

    public void tick() {
        if (isLeftArmBlocked()){
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
        if (isRightArmBlocked()){
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
        if (noLegs()){
            entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y > 0 ? entity.getDeltaMovement().y / 2 : entity.getDeltaMovement().y, entity.getDeltaMovement().z);
            if (noArms()){
                entity.setDeltaMovement(Vector3d.ZERO);
            }
        }
        else if (isLeftLegBlocked() || isRightLegBlocked()) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.abilities.flying = false;
            }
            entity.setDeltaMovement(entity.isOnGround() ? 0 : entity.getDeltaMovement().x, entity.getDeltaMovement().y, entity.isOnGround() ? 0 : entity.getDeltaMovement().z);
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("RightArm", rightArmBlocked);
        nbt.putBoolean("LeftArm", leftArmBlocked);
        nbt.putBoolean("RightLeg", rightLegBlocked);
        nbt.putBoolean("LeftLeg", leftLegBlocked);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        rightArmBlocked = nbt.getBoolean("RightArm");
        leftArmBlocked = nbt.getBoolean("LeftArm");
        rightLegBlocked = nbt.getBoolean("RightLeg");
        leftLegBlocked = nbt.getBoolean("LeftLeg");
    }
}
