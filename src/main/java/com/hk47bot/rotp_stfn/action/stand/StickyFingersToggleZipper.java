package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import com.hk47bot.rotp_stfn.init.InitSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.hk47bot.rotp_stfn.action.stand.StickyFingersRemoveZipper.getTargetedBlockPos;


public class StickyFingersToggleZipper extends StandAction {
    public StickyFingersToggleZipper(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (target.getType() == ActionTarget.TargetType.BLOCK) {
            BlockPos targetedBlockPos = getTargetedBlockPos(target, user);
            EntityZipperCapability zipperCap = user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
            if (zipperCap != null) {
                if (StickyFingersPlaceZipper.getTargetedFace(target, user) == Direction.DOWN && zipperCap.isInGround()) {
                    targetedBlockPos = targetedBlockPos.below();
                }
            }
            BlockState targetedState = world.getBlockState(targetedBlockPos);
            if (targetedState.getBlock() == InitBlocks.STICKY_FINGERS_ZIPPER.get()) {
                if (!world.isClientSide()) {
                    SoundEvent voiceline = targetedState.getValue(StickyFingersZipperBlock2.OPEN) ? InitSounds.BRUNO_ZIP.get() : InitSounds.BRUNO_UNZIP.get();
                    JojoModUtil.sayVoiceLine(user, voiceline);
                    toggleZipper(world, targetedBlockPos, targetedState);
                } else if (ClientUtil.canHearStands()) {
                    SoundEvent sound = targetedState.getValue(StickyFingersZipperBlock2.OPEN) ? InitSounds.ZIPPER_OPEN.get() : InitSounds.ZIPPER_CLOSE.get();
                    world.playLocalSound(user.getX(), user.getY(0.5), user.getZ(), sound,
                            SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
                }
            }
        }
    }

    public static void toggleZipper(World world, BlockPos targetedBlockPos, BlockState targetedState){
        BlockPos linkedPos = StickyFingersZipperBlock2.getLinkedZipperBlockPos(targetedState, targetedBlockPos, world);
        BlockState linkedState = world.getBlockState(StickyFingersZipperBlock2.getLinkedZipperBlockPos(targetedState, targetedBlockPos, world));
        world.setBlockAndUpdate(targetedBlockPos,
                targetedState.setValue(StickyFingersZipperBlock2.OPEN, !targetedState.getValue(StickyFingersZipperBlock2.OPEN)));
        if (linkedPos != targetedBlockPos) {
            world.setBlockAndUpdate(StickyFingersZipperBlock2.getLinkedZipperBlockPos(targetedState, targetedBlockPos, world),
                    linkedState.setValue(StickyFingersZipperBlock2.OPEN, !targetedState.getValue(StickyFingersZipperBlock2.OPEN)));
        }
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
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
            if (user.level.getBlockState(targetedBlockPos).getBlock() == InitBlocks.STICKY_FINGERS_ZIPPER.get()) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }
}


