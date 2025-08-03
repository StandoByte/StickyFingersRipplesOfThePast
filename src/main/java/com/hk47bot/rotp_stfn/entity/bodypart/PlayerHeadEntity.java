package com.hk47bot.rotp_stfn.entity.bodypart;

import com.github.standobyte.jojo.client.ClientUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerHeadEntity extends BodyPartEntity {

    public PlayerHeadEntity(EntityType<? extends PlayerHeadEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public PlayerHeadEntity(World world, LivingEntity owner) {
        super(InitEntities.PLAYER_HEAD.get(), world);
        setInvulnerable(true);
        setOwner(owner);
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            cap.setHead(true);
            this.remove();
        });
        return super.mobInteract(player, hand);
    }
}
