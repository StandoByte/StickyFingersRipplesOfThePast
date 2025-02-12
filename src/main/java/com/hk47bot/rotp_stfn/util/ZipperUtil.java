package com.hk47bot.rotp_stfn.util;

import com.hk47bot.rotp_stfn.capability.EntityZipperData;
import com.hk47bot.rotp_stfn.capability.EntityZipperDataProvider;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock.DIRECTION;

public class ZipperUtil {

    public static void stopMovementInZipper(Direction direction, Entity entity) {
        if (direction == Direction.DOWN || direction == Direction.UP) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
        }
        else if (direction == Direction.WEST || direction == Direction.EAST) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 0));
        }
        else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 0, 1));
        }
    }

    public static BlockPos getBlockInDirection(Direction direction, BlockPos pos){
        switch (direction){
            case UP:
                return pos.above();
            case DOWN:
                return pos.below();
            case SOUTH:
                return pos.south();
            case EAST:
                return pos.east();
            case WEST:
                return pos.west();
            default:
                return pos.north();
        }
    }
}
