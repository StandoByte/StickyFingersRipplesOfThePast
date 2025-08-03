package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import com.hk47bot.rotp_stfn.init.InitSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;



public class StickyFingersToggleZipper extends StandAction {

    public StickyFingersToggleZipper(StandAction.Builder builder){super(builder);}

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        RayTraceResult rayTraceResult = JojoModUtil.rayTrace(user, 5, null);
        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
            BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
            BlockState targetedState = world.getBlockState(targetedBlockPos);
            BlockState linkedState = world.getBlockState(StickyFingersZipperBlock2.getLinkedBlockPos(targetedState, targetedBlockPos, world));
            if (targetedState.getBlock() == InitBlocks.STICKY_FINGERS_ZIPPER.get()) {
                if (!world.isClientSide()){
                    SoundEvent voiceline = targetedState.getValue(StickyFingersZipperBlock2.OPEN) ? InitSounds.BRUNO_ZIP.get() : InitSounds.BRUNO_UNZIP.get();
                    JojoModUtil.sayVoiceLine(user, voiceline);
                    world.setBlockAndUpdate(targetedBlockPos,
                            targetedState.setValue(StickyFingersZipperBlock2.OPEN, !targetedState.getValue(StickyFingersZipperBlock2.OPEN)));
                    world.setBlockAndUpdate(StickyFingersZipperBlock2.getLinkedBlockPos(targetedState, targetedBlockPos, world),
                            linkedState.setValue(StickyFingersZipperBlock2.OPEN, !targetedState.getValue(StickyFingersZipperBlock2.OPEN)));
                }
                else if (ClientUtil.canHearStands()) {
                        SoundEvent sound = targetedState.getValue(StickyFingersZipperBlock2.OPEN) ? InitSounds.ZIPPER_OPEN.get() : InitSounds.ZIPPER_CLOSE.get();
                        world.playLocalSound(user.getX(), user.getY(0.5), user.getZ(), sound,
                                SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
                }
            }
        }
    }
}


