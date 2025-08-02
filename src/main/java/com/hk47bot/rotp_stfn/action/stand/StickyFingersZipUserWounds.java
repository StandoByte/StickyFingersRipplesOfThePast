package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import com.hk47bot.rotp_stfn.init.InitEffects;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.EntityRemoveHeadPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class StickyFingersZipUserWounds extends StandAction {
    public StickyFingersZipUserWounds(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.addEffect(new EffectInstance(InitEffects.ZIP_WOUNDS.get(), 1200, 0, false, false, true));
        if (!world.isClientSide()) {
            EntityZipperCapability capability = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            capability.setHead(false);
            PlayerHeadEntity head = new PlayerHeadEntity(world, user);
            head.moveTo(user.position());
            world.addFreshEntity(head);
            AddonPackets.sendToClientsTrackingAndSelf(new EntityRemoveHeadPacket(head.getId()), user);
        }
    }
}
