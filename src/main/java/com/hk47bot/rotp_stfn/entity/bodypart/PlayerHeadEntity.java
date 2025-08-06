package com.hk47bot.rotp_stfn.entity.bodypart;

import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PlayerHeadEntity extends BodyPartEntity {
    public PlayerHeadEntity(EntityType<? extends PlayerHeadEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public PlayerHeadEntity(World world, LivingEntity owner) {
        super(InitEntities.PLAYER_HEAD.get(), world);
        setOwner(owner);

        if (owner != null) {
            owner.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                cap.setHeadId(this.getId());
            });
        }
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (player == getOwner()) {
            player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                cap.setHead(true);
                this.remove();
            });
            return ActionResultType.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }
}
