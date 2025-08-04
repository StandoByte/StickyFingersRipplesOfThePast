package com.hk47bot.rotp_stfn.util;

import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.Arrays;
import java.util.List;

public class ZipperUtil {

//    public static void stopMovementInZipper(Direction direction, Entity entity) {
//        if (direction == Direction.DOWN || direction == Direction.UP) {
//            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 1, 0));
//        }
//        else if (direction == Direction.WEST || direction == Direction.EAST) {
//            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 0));
//        }
//        else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
//            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0, 0, 1));
//        }
//    }

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

    public static BlockPos getDiagonalNeighbor(Direction direction1, Direction direction2, BlockPos pos){
        return pos.relative(direction1).relative(direction2);
    }
    public static boolean hasZippersAround(BlockPos pos, IBlockReader world){
        for (Direction direction : Direction.values()){
            if (world.getBlockState(pos.relative(direction)).getBlock() instanceof StickyFingersZipperBlock2
                    && (world.getBlockState(pos.relative(direction)).getValue(StickyFingersZipperBlock2.INITIAL_FACING) == direction)
                    && world.getBlockState(pos.relative(direction)).getValue(StickyFingersZipperBlock.OPEN)){
                return true;
            }
        }
        return false;
    }

    public static List<Direction> X_AXIS_DIRECTIONS = Arrays.asList(Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH);
    public static List<Direction> Y_AXIS_DIRECTIONS = Arrays.asList(Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH);
    public static List<Direction> Z_AXIS_DIRECTIONS = Arrays.asList(Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST);

    public static List<Direction> getDirectionListByAxis(Direction.Axis axis){
        switch (axis){
            case X:
                return X_AXIS_DIRECTIONS;
            case Y:
                return Y_AXIS_DIRECTIONS;
            case Z:
                return Z_AXIS_DIRECTIONS;
            default:
                return null;
        }
    }

    public static Direction rotateClockwise(Direction direction, Direction.Axis axis){
        Direction result = Direction.UP;
        switch (axis){
            case X:
                switch (direction){
                    case UP:
                        result = Direction.NORTH;
                        break;
                    case NORTH:
                        result = Direction.DOWN;
                        break;
                    case DOWN:
                        result = Direction.SOUTH;
                        break;
                    case SOUTH:
                        break;
                }
                break;
            case Y:
                switch (direction){
                    case WEST:
                        result = Direction.NORTH;
                        break;
                    case NORTH:
                        result = Direction.EAST;
                        break;
                    case EAST:
                        result = Direction.SOUTH;
                        break;
                    case SOUTH:
                        result = Direction.WEST;
                        break;
                }
                break;
            case Z:
                switch (direction){
                    case UP:
                        result = Direction.WEST;
                        break;
                    case WEST:
                        result = Direction.DOWN;
                        break;
                    case DOWN:
                        result = Direction.EAST;
                        break;
                    case EAST:
                        break;
                }
                break;

        }
        return result;
    }

    public static Direction rotateCounterClockwise(Direction direction, Direction.Axis axis){
        Direction result = Direction.UP;
        switch (axis){
            case X:
                switch (direction){
                    case UP:
                        result = Direction.SOUTH;
                        break;
                    case SOUTH:
                        result = Direction.DOWN;
                        break;
                    case DOWN:
                        result = Direction.NORTH;
                        break;
                    case NORTH:
                        break;
                }
                break;
            case Y:
                switch (direction){
                    case WEST:
                        result = Direction.SOUTH;
                        break;
                    case SOUTH:
                        result = Direction.EAST;
                        break;
                    case EAST:
                        result = Direction.NORTH;
                        break;
                    case NORTH:
                        result = Direction.WEST;
                        break;
                }
                break;
            case Z:
                switch (direction){
                    case UP:
                        result = Direction.EAST;
                        break;
                    case EAST:
                        result = Direction.DOWN;
                        break;
                    case DOWN:
                        result = Direction.WEST;
                        break;
                    case WEST:
                        break;
                }
                break;

        }
        return result;
    }
}
