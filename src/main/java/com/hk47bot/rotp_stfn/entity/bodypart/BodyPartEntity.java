package com.hk47bot.rotp_stfn.entity.bodypart;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.util.mc.EntityOwnerResolver;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BodyPartEntity extends CreatureEntity implements IEntityAdditionalSpawnData {

    public EntityOwnerResolver owner = new EntityOwnerResolver();

    public BodyPartEntity(EntityType<? extends BodyPartEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public static AttributeModifierMap.MutableAttribute createBodyPartAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public void setOwner(LivingEntity entity) {
        owner.setOwner(entity);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt){
        owner.loadNbt(nbt, "OwnerId");
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt){
        owner.saveNbt(nbt, "OwnerId");
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getOwner() == null || !this.getOwner().isAlive()){
            this.remove();
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        return owner.getEntityLiving(this.level);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        owner.writeNetwork(buffer);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        owner.readNetwork(additionalData);
        if (this instanceof PlayerHeadEntity && this.getOwner() == ClientUtil.getClientPlayer()){
            ClientUtil.setCameraEntityPreventShaderSwitch(this);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
