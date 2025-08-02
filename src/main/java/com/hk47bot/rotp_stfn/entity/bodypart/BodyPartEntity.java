package com.hk47bot.rotp_stfn.entity.bodypart;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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

public class BodyPartEntity extends CreatureEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(BodyPartEntity.class, DataSerializers.OPTIONAL_UUID);

    public BodyPartEntity(EntityType<? extends BodyPartEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public static AttributeModifierMap.MutableAttribute createBodyPartAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 3.0D);
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
        super.defineSynchedData();
        entityData.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt){
        UUID ownerId = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        if (ownerId != null) {
            setOwnerUUID(ownerId);
        }
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt){
        if (getOwnerUUID() != null) {
            nbt.putUUID("Owner", getOwnerUUID());
        }
        super.addAdditionalSaveData(nbt);
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
