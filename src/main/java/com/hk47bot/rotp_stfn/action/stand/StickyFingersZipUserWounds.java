package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.init.InitEffects;
import com.hk47bot.rotp_stfn.init.InitSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class StickyFingersZipUserWounds extends StandAction {
    public StickyFingersZipUserWounds(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.addEffect(new EffectInstance(InitEffects.ZIP_WOUNDS.get(), 1200, 0, false, false, true));
        if (ClientUtil.canHearStands()) {
            world.playLocalSound(user.getX(), user.getY(0.5), user.getZ(), InitSounds.ZIPPER_CLOSE.get(),
                    SoundCategory.PLAYERS, 1.0F, 1.0F, false);
        }
    }
}
