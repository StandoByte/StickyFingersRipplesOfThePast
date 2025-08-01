package com.hk47bot.rotp_stfn.entity.bodypart;

import com.hk47bot.rotp_stfn.init.InitEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class PlayerArmEntity extends BodyPartEntity {

    public PlayerArmEntity(EntityType<? extends PlayerArmEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public PlayerArmEntity(World world, LivingEntity owner) {
        super(InitEntities.PLAYER_ARM.get(), world);
        this.setOwnerUUID(owner.getUUID());
        this.setInvulnerable(true);
    }
}
