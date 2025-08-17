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
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.hk47bot.rotp_stfn.action.stand.StickyFingersRemoveZipper.getTargetedBlockPos;

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
            LivingEntity user = power.getUser();
            RayTraceResult rayTraceResult = JojoModUtil.rayTrace(power.getUser(), 5, null);
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos targetedBlockPos = getTargetedBlockPos(new ActionTarget(blockRayTraceResult.getBlockPos(), blockRayTraceResult.getDirection()), power.getUser());
                EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
                if (zipperCap != null) {
                    if (getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()) {
                        targetedBlockPos = targetedBlockPos.below();
                    }
                }
                if (ZipperUtil.isBlockZipper(power.getUser().level, targetedBlockPos)) {
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
            EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (zipperCap != null) {
                if (StickyFingersPlaceZipper.getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()) {
                    targetedBlockPos = targetedBlockPos.below();
                }
            }
            if (ZipperUtil.isBlockZipper(user.level, targetedBlockPos)) {
                return ActionConditionResult.NEGATIVE_CONTINUE_HOLD;
            }
            return ActionConditionResult.POSITIVE;
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        BlockPos targetedBlockPos = target.getBlockPos();
        if (target.getType() == ActionTarget.TargetType.BLOCK){
            EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (zipperCap != null){
                if (getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()){
                    targetedBlockPos = targetedBlockPos.below();
                }
                BlockState targetedBlockState = user.level.getBlockState(targetedBlockPos);
                BlockPos zipperBlockPos = targetedBlockPos.relative(getTargetedFace(target, user));
                BlockPos linkedZipperBlockPos = StickyFingersZipperBlock2.getLinkedBlockPos(zipperBlockPos, user.level, getTargetedFace(target, user).getOpposite());
                if (targetedBlockState.isFaceSturdy(user.level, targetedBlockPos, getTargetedFace(target, user))
                        && ZipperUtil.isBlockFree(user.level, zipperBlockPos)) {
                    if (!ZipperUtil.isBlockZipper(world, targetedBlockPos) && ticksHeld > 0) {
                        if (user.level.getBlockState(linkedZipperBlockPos.relative(getTargetedFace(target, user))).isFaceSturdy(user.level, linkedZipperBlockPos.relative(getTargetedFace(target, user)), getTargetedFace(target, user).getOpposite())
                                && ZipperUtil.isBlockFree(user.level, linkedZipperBlockPos)){
                            StickyFingersZipperBlock2.placeZippers(world, targetedBlockPos, getTargetedFace(target, user), user);
                        }
                        else {
                            StickyFingersZipperBlock2.placeSingleZipper(world, targetedBlockPos, getTargetedFace(target, user), user);
                            if (getTargetedFace(target, user).getAxis().isHorizontal() && zipperCap.isInGround() && (ZipperUtil.isBlockFree(world, zipperBlockPos.below()) || ZipperUtil.isBlockFree(world, zipperBlockPos.above()))){
                                StickyFingersZipperBlock2.placeSingleZipper(world, ZipperUtil.isBlockFree(world, zipperBlockPos.below()) ? targetedBlockPos.below() : ZipperUtil.isBlockFree(world, zipperBlockPos.above()) ? targetedBlockPos.above() : targetedBlockPos, getTargetedFace(target, user), user);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Direction getTargetedFace(ActionTarget target, LivingEntity user){
        EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
        if (zipperCap != null && zipperCap.isInGround() && target.getType() == ActionTarget.TargetType.BLOCK){
            return target.getFace().getOpposite();
        }

        return target.getFace();
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
    }
}
