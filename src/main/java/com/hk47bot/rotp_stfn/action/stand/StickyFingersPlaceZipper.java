package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.render.entity.animnew.stand.StandActionAnimation;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StickyFingersPlaceZipper extends StandAction {
    public static final StandPose STAND_POSE = new StandPose("attack") {
        @Override
        public StandActionAnimation getAnim(List<StandActionAnimation> variants, StandEntity standEntity) {
            return standEntity != null ? variants.get((standEntity.punchComboCount - 1) % variants.size()) : super.getAnim(variants, standEntity);
        }
    };

    public StickyFingersPlaceZipper(StandAction.Builder builder) {
        super(builder);
    }

    @Nullable
    @Override
    public Action<IStandPower> getVisibleAction(IStandPower power, ActionTarget target) {
        if (power.getHeldActionTicks() == 0) {
            RayTraceResult rayTraceResult = JojoModUtil.rayTrace(power.getUser(), 5, null);
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
                if (isBlockZipper(power.getUser().level, targetedBlockPos)) {
                    return InitStands.STICKY_FINGERS_REMOVE_ZIPPER.get();
                }
            }
        }
        return this;
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (target.getType() == ActionTarget.TargetType.BLOCK) {
            BlockPos targetedBlockPos = target.getBlockPos();
            if (isBlockZipper(user.level, targetedBlockPos)) {
                return ActionConditionResult.NEGATIVE_CONTINUE_HOLD;
            }
            BlockState targetedBlockState = user.level.getBlockState(targetedBlockPos);
            BlockPos zipperBlockPos = targetedBlockPos.relative(target.getFace());
            BlockPos linkedZipperBlockPos = StickyFingersZipperBlock2.getLinkedBlockPos(zipperBlockPos, user.level, target.getFace().getOpposite());
            if (targetedBlockState.isFaceSturdy(user.level, targetedBlockPos, target.getFace())
                    && user.level.getBlockState(linkedZipperBlockPos.relative(target.getFace())).isFaceSturdy(user.level, linkedZipperBlockPos.relative(target.getFace()), target.getFace().getOpposite())
                    && isBlockFree(user.level, zipperBlockPos)
                    && isBlockFree(user.level, linkedZipperBlockPos)) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (target.getType() == ActionTarget.TargetType.BLOCK) {
            BlockPos targetedBlockPos = target.getBlockPos();
            if (!isBlockZipper(world, targetedBlockPos) && ticksHeld > 0){
                if (isBlockFree(world, targetedBlockPos.relative(target.getFace()))) {
                    StickyFingersZipperBlock2.placeZippers(world, targetedBlockPos, target.getFace(), user);
                }
            }
        }
    }

    public static boolean isBlockFree(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() instanceof AirBlock
                || world.getBlockState(blockPos).getMaterial().isReplaceable()
                || world.getBlockState(blockPos).getCollisionShape(world, blockPos).equals(VoxelShapes.empty());
    }

    public static boolean isBlockZipper(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() instanceof StickyFingersZipperBlock2;
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
    }
}
