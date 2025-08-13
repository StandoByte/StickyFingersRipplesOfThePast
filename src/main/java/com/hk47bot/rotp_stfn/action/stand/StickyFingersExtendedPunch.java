package com.hk47bot.rotp_stfn.action.stand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import  com.hk47bot.rotp_stfn.entity.projectile.ExtendedPunchEntity;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class StickyFingersExtendedPunch extends StandEntityAction {
    public StickyFingersExtendedPunch(StandEntityAction.Builder builder) {
        super(builder);
    }
    @Override
    protected ActionConditionResult checkStandConditions(StandEntity stand, IStandPower power, ActionTarget target) {
        if ((stand instanceof StickyFingersEntity && !((StickyFingersEntity) stand).hasForeArm()) && (stand instanceof StickyFingersEntity && !((StickyFingersEntity) stand).hasShortForeArm())) {
            return conditionMessage("stickyfingers_rightforearm");
        }
        return super.checkStandConditions(stand, power, target);
    }

    @Nonnull
    protected ResourceLocation getIconTexturePath(@Nullable IStandPower power) {
        return !power.getUser().isShiftKeyDown() ? super.getIconTexturePath(power) : new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/action/sticky_fingers_extended_punch_pull.png");
    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        super.onTaskSet(world, standEntity, standPower, phase, task, ticks);
        if (!world.isClientSide()) {
            StickyFingersEntity stickyfingers = (StickyFingersEntity) standEntity;
            ExtendedPunchEntity rightforearm = new ExtendedPunchEntity(world, standEntity, standPower);
            if (standPower.getUser().isShiftKeyDown()) {
                rightforearm.setBindEntities(true);
            }
            standEntity.addProjectile(rightforearm);
            stickyfingers.setForeArm(false);
            stickyfingers.setShortForeArm(false);
        }
    }

    @Override
    protected void onTaskStopped(World world, StandEntity standEntity, IStandPower standPower, StandEntityTask task, @Nullable StandEntityAction newAction) {
        if (!world.isClientSide()) {
            StickyFingersEntity stickyfingers = (StickyFingersEntity) standEntity;
            takeForeArm(stickyfingers);
        }
    }

    public void takeForeArm(StickyFingersEntity stickyfingers) {
        stickyfingers.setForeArm(true);
        stickyfingers.setShortForeArm(true);
    }
}

