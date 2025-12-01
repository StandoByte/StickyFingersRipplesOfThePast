package com.hk47bot.rotp_stfn.entity.bodypart;

import com.github.standobyte.jojo.client.ClientUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class UnzippedHeadEntity extends BodyPartEntity {
    public UnzippedHeadEntity(EntityType<? extends UnzippedHeadEntity> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public UnzippedHeadEntity(World world, LivingEntity owner) {
        super(InitEntities.PLAYER_HEAD.get(), world);
        setOwner(owner);

        if (owner != null) {
            owner.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                cap.setHeadId(this.getUUID());
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

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        super.readSpawnData(additionalData);
        if (this.getOwner() == ClientUtil.getClientPlayer()){
            ClientUtil.setCameraEntityPreventShaderSwitch(this);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (owner.getEntity(level) != null){
            this.setAirSupply(owner.getEntity(level).getAirSupply());
        }
    }
}
