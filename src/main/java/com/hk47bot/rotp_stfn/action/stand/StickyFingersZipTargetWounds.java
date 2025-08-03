package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitEffects;
import com.hk47bot.rotp_stfn.init.InitSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class StickyFingersZipTargetWounds extends StandAction {
    public StickyFingersZipTargetWounds(StandAction.Builder builder) {
        super(builder);
    }

    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.ENTITY;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        Entity entity = target.getEntity();
        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.addEffect(new EffectInstance(InitEffects.ZIP_WOUNDS.get(), 1200, 0, false, false, true));
            if (ClientUtil.canHearStands()) {
                world.playLocalSound(livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ(), InitSounds.ZIPPER_CLOSE.get(),
                        SoundCategory.PLAYERS, 1.0F, 1.0F, false);
            }
        }
    }
}
