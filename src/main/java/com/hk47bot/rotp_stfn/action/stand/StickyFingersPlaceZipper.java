package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class StickyFingersPlaceZipper extends StandEntityAction {
    public StickyFingersPlaceZipper(StandEntityAction.Builder builder){super(builder);}

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        RayTraceResult rayTraceResult = JojoModUtil.rayTrace(power.getUser(), 5, null);
        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
            BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
            if (user.level.getBlockState(targetedBlockPos).isFaceSturdy(user.level, targetedBlockPos, blockRayTraceResult.getDirection())
                    && isBlockFree(user.level, targetedBlockPos.relative(blockRayTraceResult.getDirection()))
                    && (isBlockFree(user.level, targetedBlockPos.relative(blockRayTraceResult.getDirection().getOpposite()))
                    || isBlockFree(user.level, targetedBlockPos.relative(blockRayTraceResult.getDirection().getOpposite(), 2)))) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()){
            RayTraceResult rayTraceResult = JojoModUtil.rayTrace(userPower.getUser(), 5, null);
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
                if (isBlockFree(world, targetedBlockPos.relative(blockRayTraceResult.getDirection()))) {
                    StickyFingersZipperBlock2.placeZippers(world, targetedBlockPos, blockRayTraceResult.getDirection());
                }
            }
        }
    }
    public static boolean isBlockFree(World world, BlockPos blockPos){
        return world.getBlockState(blockPos).getBlock() instanceof AirBlock || world.getBlockState(blockPos).getMaterial().isReplaceable();
    }
    public static boolean isBlockZipper(World world, BlockPos blockPos){
        return world.getBlockState(blockPos).getBlock() instanceof StickyFingersZipperBlock2;
    }
}
