package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.hk47bot.rotp_stfn.action.stand.StickyFingersPlaceZipper.getTargetedFace;

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
            BlockPos targetedBlockPos = getTargetedBlockPos(target, user);
            EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (zipperCap != null) {
                if (StickyFingersPlaceZipper.getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()) {
                    targetedBlockPos = targetedBlockPos.below();
                }
            }
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
                BlockPos targetedBlockPos = getTargetedBlockPos(target, user);
                EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
                if (zipperCap != null) {
                    if (getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()) {
                        targetedBlockPos = targetedBlockPos.below();
                    }
                }
                if (isBlockZipper(world, targetedBlockPos)) {
                    world.getBlockState(targetedBlockPos).getBlock().destroy(world, targetedBlockPos, world.getBlockState(targetedBlockPos));
                    if (getTargetedFace(target, user).getAxis().isHorizontal() && zipperCap.isInGround() && (ZipperUtil.isBlockZipper(world, targetedBlockPos.below()) || ZipperUtil.isBlockZipper(world, targetedBlockPos.above()))){
                        targetedBlockPos = ZipperUtil.isBlockZipper(world, targetedBlockPos.below()) ? targetedBlockPos.below() : targetedBlockPos.above();
                        world.getBlockState(targetedBlockPos).getBlock().destroy(world, targetedBlockPos, world.getBlockState(targetedBlockPos));
                    }
                }
            }
        }
    }

    public static BlockPos getTargetedBlockPos(ActionTarget target, LivingEntity user){
        EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
        if (zipperCap != null && zipperCap.isInGround()){
            if (StickyFingersPlaceZipper.getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()) {
                return target.getBlockPos().below();
            }
            return target.getBlockPos().relative(getTargetedFace(target, user));
        }

        return target.getBlockPos();
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
    }

    public static boolean isBlockZipper(World world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() instanceof StickyFingersZipperBlock;
    }
}
