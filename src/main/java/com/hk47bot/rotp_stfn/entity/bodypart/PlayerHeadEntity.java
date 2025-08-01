package com.hk47bot.rotp_stfn.entity.bodypart;

import com.hk47bot.rotp_stfn.init.InitEntities;
import net.minecraft.entity.*;
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

public class PlayerHeadEntity extends BodyPartEntity {

    public PlayerHeadEntity(EntityType<? extends PlayerHeadEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public PlayerHeadEntity(World world, LivingEntity owner) {
        super(InitEntities.PLAYER_HEAD.get(), world);
        this.setOwnerUUID(owner.getUUID());
        this.setInvulnerable(true);
    }
}
