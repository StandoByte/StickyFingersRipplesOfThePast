package com.hk47bot.rotp_stfn.entity.bodypart;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PlayerArmEntity extends BodyPartEntity {

    protected static final DataParameter<Boolean> IS_RIGHT = EntityDataManager.defineId(PlayerArmEntity.class, DataSerializers.BOOLEAN);

    public PlayerArmEntity(EntityType<? extends PlayerArmEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public PlayerArmEntity(World world, LivingEntity owner) {
        super(InitEntities.PLAYER_ARM.get(), world);
        this.setOwner(owner);
        this.setInvulnerable(true);
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if (isRight()){
                cap.setRightArmBlocked(false);
            }
            else {
                cap.setLeftArmBlocked(false);
            }
            this.remove();
        });
        return super.mobInteract(player, hand);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_RIGHT, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt){
        entityData.set(IS_RIGHT, nbt.getBoolean("IsRight"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt){
        nbt.putBoolean("IsRight", entityData.get(IS_RIGHT));
        super.addAdditionalSaveData(nbt);
    }

    public void setRightOrLeft(boolean side){
        entityData.set(IS_RIGHT, side);
    }

    public boolean isRight(){
        return entityData.get(IS_RIGHT);
    }
}
