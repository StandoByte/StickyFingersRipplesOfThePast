package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class StickyFingersRemoveZipper extends StandAction {
    public StickyFingersRemoveZipper(StandAction.Builder builder){super(builder);}

    @Override
    public boolean isUnlocked(IStandPower power) {
        return InitStands.STICKY_FINGERS_PLACE_ZIPPER.get().isUnlocked(power);
    }
    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        RayTraceResult rayTraceResult = JojoModUtil.rayTrace(power.getUser(), 5, null);
        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
            BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
            if (isBlockZipper(user.level, targetedBlockPos)
                    && (isBlockZipper(user.level, targetedBlockPos.relative(blockRayTraceResult.getDirection().getOpposite(), 2))
                    || isBlockZipper(user.level, targetedBlockPos.relative(blockRayTraceResult.getDirection().getOpposite(), 3)))) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()){
            RayTraceResult rayTraceResult = JojoModUtil.rayTrace(user, 5, null);
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
                if (isBlockZipper(world, targetedBlockPos)) {
                    System.out.println("I WORK");
                    world.getBlockState(targetedBlockPos).getBlock().destroy(world, targetedBlockPos, world.getBlockState(targetedBlockPos));
                }
            }
        }
    }

    public static boolean isBlockZipper(World world, BlockPos blockPos){
        return world.getBlockState(blockPos).getBlock() instanceof StickyFingersZipperBlock2;
    }
}
