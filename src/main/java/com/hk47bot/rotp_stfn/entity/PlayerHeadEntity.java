package com.hk47bot.rotp_stfn.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class PlayerHeadEntity extends MobEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(PlayerHeadEntity.class, DataSerializers.OPTIONAL_UUID);
    public PlayerHeadEntity(EntityType<? extends PlayerHeadEntity> type, World world) {
        super(type, world);
    }
    @Nullable
    protected UUID getOwnerUUID() {
        return entityData.get(OWNER_UUID).orElse(null);
    }
    protected void setOwnerUUID(@Nullable UUID uuid) {
        entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }
    @Override
    protected void defineSynchedData() {
        entityData.define(OWNER_UUID, Optional.empty());
    }
    @Override
    public void readAdditionalSaveData (CompoundNBT nbt){
        UUID ownerId = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        if (ownerId != null) {
            setOwnerUUID(ownerId);
        }
    }
    @Override
    public void addAdditionalSaveData (CompoundNBT nbt){
        if (getOwnerUUID() != null) {
            nbt.putUUID("Owner", getOwnerUUID());
        }
    }
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            if (uuid == null) return null;
            PlayerEntity owner = level.getPlayerByUUID(uuid);
            return owner;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
