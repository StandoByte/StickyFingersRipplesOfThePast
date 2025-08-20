package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.SoulEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.IPlayerPossess;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

public class StickyFingersGetInsideMob extends StandAction {
    public StickyFingersGetInsideMob(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.ENTITY;
    }

    @Override
    protected ActionConditionResult checkTarget(ActionTarget target, LivingEntity user, IStandPower power) {
        Entity targetEntity = target.getEntity();

        if (targetEntity instanceof StandEntity || !(targetEntity instanceof LivingEntity)) {
            return ActionConditionResult.NEGATIVE;
        }

        boolean checkboxes = targetEntity.getBbWidth() >= user.getBbWidth() && targetEntity.getBbHeight() >= user.getBbHeight() * 0.9F;

        if (checkboxes) {
            return ActionConditionResult.POSITIVE;
        }

        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide() && user instanceof IPlayerPossess) {
            Entity targetEntity = target.getEntity();
            ((IPlayerPossess)user).jojoPossessEntity(targetEntity, true, this);
        }
    }
}


