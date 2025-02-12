package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StickyFingersPlaceZipper extends StandEntityAction {
    public StickyFingersPlaceZipper(StandEntityAction.Builder builder){super(builder.standOffsetFront().holdType());}
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {

    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()){
            RayTraceResult rayTraceResult = JojoModUtil.rayTrace(standEntity, 5, null);
            if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                BlockPos targetedBlockPos = blockRayTraceResult.getBlockPos();
                if (world.getBlockState(targetedBlockPos).getBlock() != Blocks.AIR && world.getBlockState(targetedBlockPos).getBlock() != InitBlocks.STICKY_FINGERS_ZIPPER.get()) {
                    switch (blockRayTraceResult.getDirection()) {
                        case NORTH:
                        case SOUTH:
                            world.setBlockAndUpdate(targetedBlockPos.north(), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(StickyFingersZipperBlock.DIRECTION, Direction.NORTH));
                            world.setBlockAndUpdate(targetedBlockPos.south(), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(StickyFingersZipperBlock.DIRECTION, Direction.SOUTH));
                            break;
                        case EAST:
                        case WEST:
                            world.setBlockAndUpdate(targetedBlockPos.west(), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(StickyFingersZipperBlock.DIRECTION, Direction.WEST));
                            world.setBlockAndUpdate(targetedBlockPos.east(), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(StickyFingersZipperBlock.DIRECTION, Direction.EAST));
                            break;
                        case UP:
                        case DOWN:
                            world.setBlockAndUpdate(targetedBlockPos.below(), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(StickyFingersZipperBlock.DIRECTION, Direction.DOWN));
                            world.setBlockAndUpdate(targetedBlockPos.above(), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(StickyFingersZipperBlock.DIRECTION, Direction.UP));
                            break;
                    }
                }
            }
        }
    }
}
