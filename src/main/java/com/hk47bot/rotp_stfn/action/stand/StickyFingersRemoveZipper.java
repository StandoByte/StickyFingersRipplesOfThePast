package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StickyFingersRemoveZipper extends StandAction {
    public StickyFingersRemoveZipper(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    public boolean isUnlocked(IStandPower power) {
        return InitStands.STICKY_FINGERS_PLACE_ZIPPER.get().isUnlocked(power);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (target.getType() == ActionTarget.TargetType.BLOCK) {
            BlockPos targetedBlockPos = target.getBlockPos();
            if (!(power.getHeldAction() instanceof StickyFingersPlaceZipper)
                    && isBlockZipper(user.level, targetedBlockPos)) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            if (target.getType() == ActionTarget.TargetType.BLOCK) {
                BlockPos targetedBlockPos = target.getBlockPos();
                if (isBlockZipper(world, targetedBlockPos)) {
                    world.getBlockState(targetedBlockPos).getBlock().destroy(world, targetedBlockPos, world.getBlockState(targetedBlockPos));
                }
            }
        }
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
    }

    public static boolean isBlockZipper(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() instanceof StickyFingersZipperBlock2;
    }
}
