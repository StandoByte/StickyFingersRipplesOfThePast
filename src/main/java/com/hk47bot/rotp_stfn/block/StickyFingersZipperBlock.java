package com.hk47bot.rotp_stfn.block;

import com.hk47bot.rotp_stfn.action.stand.StickyFingersPlaceZipper;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.*;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.*;


public class StickyFingersZipperBlock extends SixWayBlock implements IWaterLoggable, ITileEntityProvider {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 1, 4);
    // 1 - 0 гр, 2 - 90 гр, 3 - 180 гр, 4 - 270 гр
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 4);
    // 1 - прямо, 2 - г, 3 - т, 4 - х
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty RIGHT_UP = BooleanProperty.create("right_up");
    public static final BooleanProperty LEFT_UP = BooleanProperty.create("left_up");
    public static final BooleanProperty RIGHT_DOWN = BooleanProperty.create("right_down");
    public static final BooleanProperty LEFT_DOWN = BooleanProperty.create("left_down");

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public StickyFingersZipperBlock(Properties properties) {
        super(1, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH)
                .setValue(ROTATION, 1)
                .setValue(TYPE, 1)
                .setValue(RIGHT_UP, Boolean.FALSE)
                .setValue(LEFT_UP, Boolean.FALSE)
                .setValue(RIGHT_DOWN, Boolean.FALSE)
                .setValue(LEFT_DOWN, Boolean.FALSE)
                .setValue(OPEN, Boolean.FALSE)

        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(WATERLOGGED);
        builder.add(ROTATION);
        builder.add(TYPE);
        builder.add(OPEN);
        builder.add(RIGHT_UP);
        builder.add(LEFT_UP);
        builder.add(RIGHT_DOWN);
        builder.add(LEFT_DOWN);



    }
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new StickyFingersZipperTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING)){
            case UP:
                return VoxelShapes.box(1, 0, 1, 0, 0.01, 0);
            case DOWN:
                return VoxelShapes.box(1, 1, 1, 0, 0.99, 0);
            case WEST:
                return VoxelShapes.box(1, 1, 1, 0.99, 0, 0);
            case EAST:
                return VoxelShapes.box(0, 1, 1, 0.01, 0, 0);
            case SOUTH:
                return VoxelShapes.box(1, 1, 0, 0, 0, 0.01);
            default:
            return VoxelShapes.box(1, 1, 1, 0, 0, 0.99);
        }

    }


//    @Override
//    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos blockPos, boolean p_220069_6_) {
//
//    }

    public Direction getDirectionFromRotation(int rotation, Direction direction){
        switch (direction){

            case UP:
                switch (rotation){
                    case 1:
                        return Direction.NORTH;
                    case 2:
                        return Direction.WEST;
                    case 3:
                        return Direction.SOUTH;
                    case 4:
                        return Direction.EAST;
                    default:
                        return Direction.UP;
                }
            case DOWN:
                switch (rotation){
                    case 1:
                        return Direction.SOUTH;
                    case 2:
                        return Direction.WEST;
                    case 3:
                        return Direction.NORTH;
                    case 4:
                        return Direction.EAST;
                    default:
                        return Direction.DOWN;
                }

            case NORTH:
                switch (rotation){
                    case 1:
                        return Direction.UP;
                    case 2:
                        return Direction.EAST;
                    case 3:
                        return Direction.DOWN;
                    case 4:
                        return Direction.WEST;
                    default:
                        return Direction.NORTH;
                }

            case SOUTH:
                switch (rotation){
                    case 1:
                        return Direction.UP;
                    case 2:
                        return Direction.WEST;
                    case 3:
                        return Direction.DOWN;
                    case 4:
                        return Direction.EAST;
                    default:
                        return Direction.SOUTH;
                }

            case EAST:
                switch (rotation){
                    case 1:
                        return Direction.UP;
                    case 2:
                        return Direction.SOUTH;
                    case 3:
                        return Direction.DOWN;
                    case 4:
                        return Direction.NORTH;
                    default:
                        return Direction.EAST;
                }

            case WEST:
                switch (rotation){
                    case 1:
                        return Direction.UP;
                    case 2:
                        return Direction.NORTH;
                    case 3:
                        return Direction.DOWN;
                    case 4:
                        return Direction.SOUTH;
                    default:
                        return Direction.WEST;
                }

        }
        return direction;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
        if (neighborState.getBlock() instanceof StickyFingersZipperBlock && world.getBlockState(getLinkedBlockPos(state, pos, world)).getBlock() instanceof StickyFingersZipperBlock){
            if (neighborState.getValue(FACING) == state.getValue(FACING)){
                world.setBlock(pos, getZipperState((World) world, pos, state).setValue(OPEN, neighborState.getValue(OPEN)), 11);
                if (world.getBlockState(getLinkedBlockPos(state, pos, world)).getValue(OPEN) != state.getValue(OPEN)){
                    world.setBlock(getLinkedBlockPos(state, pos, world), getZipperState((World) world, getLinkedBlockPos(state, pos, world), world.getBlockState(getLinkedBlockPos(state, pos, world))).setValue(OPEN, neighborState.getValue(OPEN)), 11);
                }
            }
        }
        return getZipperState((World) world, pos, state);
    }


    public static BlockPos getLinkedBlockPos(BlockState state, BlockPos pos, IWorld world){
        if (state.getBlock() instanceof StickyFingersZipperBlock){
            return pos.relative(state.getValue(FACING).getOpposite(), StickyFingersPlaceZipper.isBlockFree(world.getBlockState(pos.relative(state.getValue(FACING).getOpposite(), 2)).getBlock()) ? 2 : 3);
        }
        return pos;
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        BlockPos linkedPos = getLinkedBlockPos(state, pos, world);
        BlockState linkedState = world.getBlockState(linkedPos);
        if (linkedState.is(this)) {
            world.destroyBlock(linkedPos, true);
        }
        super.destroy(world, pos, state);
    }

    public static boolean hasDiagonalNeighbor(World world, BlockPos pos, Direction dir1, Direction dir2){
        return getDiagonalNeighbor(world, pos, dir1, dir2).getBlock() instanceof StickyFingersZipperBlock;
    }

    public static BlockState getDiagonalNeighbor(World world, BlockPos pos, Direction dir1, Direction dir2){
        return world.getBlockState(ZipperUtil.getBlockInDirection(dir1, ZipperUtil.getBlockInDirection(dir2, pos)));
    }

    public static BlockState getZipperState(World world, BlockPos pos, BlockState state){
        int connections = 0;
        boolean rightUp = false;
        boolean leftUp = false;
        boolean rightDown = false;
        boolean leftDown = false;
        BlockState newState = state;
        List<Direction> neighbors = new ArrayList<>();
        List<Direction> diagonalNeighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof StickyFingersZipperBlock) {
                if (state.getValue(FACING).getAxis() != direction.getAxis() && neighborState.getValue(FACING).getAxis() == state.getValue(FACING).getAxis()) {
                    connections++;
                    neighbors.add(direction);
                }
            }
        }
        List<Direction> stateFacing = new ArrayList<>();
        stateFacing.add(state.getValue(FACING));
        stateFacing.add(state.getValue(FACING).getOpposite());
        for (Direction direction : stateFacing){
            BlockPos offsetPos = pos.relative(direction);
            for (Direction direction1 : Direction.values()) {
                BlockPos neighborPos = offsetPos.relative(direction1);
                BlockState neighborState = world.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof StickyFingersZipperBlock) {
                    if (state.getValue(FACING).getAxis() != direction1.getAxis() && neighborState.getValue(FACING).getAxis() == state.getValue(FACING).getAxis()) {
                        connections++;
                        diagonalNeighbors.add(direction1);
                    }
                }
            }
        }

        if (!neighbors.isEmpty()) {
            //CONNECTION CORNERS

            // LEFT_DOWN
            //facing = south, neighbours = west, down
            //facing = north, neighbours = east, down
            //facing = east, neighbours = south, down
            //facing = west, neighbours = north, down
            //facing = down, neighbours = south, east
            //facing = up, neighbours =

            // LEFT_UP
            //facing = south, neighbours = west, up
            //facing = north, neighbours = east, up
            //facing = east, neighbours = north, up
            //facing = west, neighbours = south, up

            // RIGHT_DOWN
            //facing = south, neighbours = east, down
            //facing = north, neighbours = west, down
            //facing = east, neighbours = north, down
            //facing = west, neighbours = south, down

            // RIGHT_UP
            //facing = south, neighbours = east, up
            //facing = north, neighbours = west, up
            //facing = east, neighbours = north, up
            //facing = west, neighbours = south, up

            if (state.getValue(FACING).getAxis().isHorizontal()){
                //0
                if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                        && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.UP, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.UP, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.UP, Direction.WEST)
                    )){
                        if (newState.getValue(FACING) == Direction.NORTH) rightUp = true;
                        else if (newState.getValue(FACING) == Direction.SOUTH) leftUp = true;
                    }
                }
                if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                        && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.UP, Direction.SOUTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.UP, Direction.SOUTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.UP, Direction.SOUTH)
                    )) {
                        if (newState.getValue(FACING) == Direction.EAST) leftUp = true;
                        else if (newState.getValue(FACING) == Direction.WEST) rightUp = true;
                    }
                }
                //90
                if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                        && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.DOWN, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.DOWN, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.DOWN, Direction.WEST)
                    )) {
                        if (newState.getValue(FACING) == Direction.NORTH) rightDown = true;
                        else if (newState.getValue(FACING) == Direction.SOUTH) leftDown = true;
                    }
                }
                if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                        && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.DOWN, Direction.SOUTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.DOWN, Direction.SOUTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.DOWN, Direction.SOUTH)
                    )) {
                        if (newState.getValue(FACING) == Direction.EAST) leftDown = true;
                        else if (newState.getValue(FACING) == Direction.WEST) rightDown = true;
                    }
                }
                //180
                if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                        && (neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.DOWN, Direction.EAST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.DOWN, Direction.EAST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.DOWN, Direction.EAST)
                    )) {
                        if (newState.getValue(FACING) == Direction.NORTH) leftDown = true;
                        else if (newState.getValue(FACING) == Direction.SOUTH) rightDown = true;
                    }
                }
                if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                        && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.DOWN, Direction.NORTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.DOWN, Direction.NORTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.DOWN, Direction.NORTH)
                    )) {
                        if (newState.getValue(FACING) == Direction.EAST) rightDown = true;
                        else if (newState.getValue(FACING) == Direction.WEST) leftDown = true;
                    }
                }
                //270
                if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                        && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.UP, Direction.EAST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.UP, Direction.EAST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.UP, Direction.EAST)
                    )) {
                        if (newState.getValue(FACING) == Direction.NORTH) leftUp = true;
                        else if (newState.getValue(FACING) == Direction.SOUTH) rightUp = true;
                    }
                }
                if ((neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))
                        && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.UP, Direction.NORTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.UP, Direction.NORTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.UP, Direction.NORTH)
                    )) {
                        if (newState.getValue(FACING) == Direction.EAST) rightUp = true;
                        else if (newState.getValue(FACING) == Direction.WEST) leftUp = true;
                    }
                }
            }
            else {
                if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                        && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.EAST, Direction.NORTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.EAST, Direction.NORTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.EAST, Direction.NORTH)
                    )) {
                        if (newState.getValue(FACING) == Direction.UP) rightUp = true;
                        else if (newState.getValue(FACING) == Direction.DOWN) leftUp = true;
                    }
                }
                if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                        && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.EAST, Direction.SOUTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.EAST, Direction.SOUTH)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.EAST, Direction.SOUTH)
                    )) {
                        if (newState.getValue(FACING) == Direction.UP) rightDown = true;
                        else if (newState.getValue(FACING) == Direction.DOWN) leftDown = true;
                    }
                }
                if ((neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))
                        && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.SOUTH, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.SOUTH, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.SOUTH, Direction.WEST)
                    )) {
                        if (newState.getValue(FACING) == Direction.UP) leftDown = true;
                        else if (newState.getValue(FACING) == Direction.DOWN) rightDown = true;
                    }
                }
                if ((neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))
                        && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                    if (!(hasDiagonalNeighbor(world, pos, Direction.NORTH, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING)), Direction.NORTH, Direction.WEST)
                            || hasDiagonalNeighbor(world, pos.relative(state.getValue(FACING).getOpposite()), Direction.NORTH, Direction.WEST)

                    )) {
                        if (newState.getValue(FACING) == Direction.UP) leftUp = true;
                        else if (newState.getValue(FACING) == Direction.DOWN) rightUp = true;
                    }
                }
            }

            // TYPES
            switch (connections) {
                case 4:
                    newState = newState
                            .setValue(TYPE, 4)
                            .setValue(ROTATION, 1);
                    break;
                case 3:
                    newState = newState.setValue(TYPE, 3);
                    if (state.getValue(FACING).getAxis().isHorizontal()){
                        //0
                        if ((newState.getValue(FACING) == Direction.NORTH || newState.getValue(FACING) == Direction.SOUTH)
                                && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                                && (neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            newState = newState.setValue(ROTATION, 1);
                        }
                        else if ((newState.getValue(FACING) == Direction.EAST || newState.getValue(FACING) == Direction.WEST)
                                && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                            newState = newState.setValue(ROTATION, 1);
                        }
                        //90
                        else if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                                && (neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            if (newState.getValue(FACING) == Direction.NORTH) newState = newState.setValue(ROTATION, 4);
                            else if (newState.getValue(FACING) == Direction.SOUTH) newState = newState.setValue(ROTATION, 2);
                        }
                        else if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                                && (neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                            if (newState.getValue(FACING) == Direction.EAST) newState = newState.setValue(ROTATION, 4);
                            else if (newState.getValue(FACING) == Direction.WEST) newState = newState.setValue(ROTATION, 2);

                        }
                        //180
                        else if ((newState.getValue(FACING) == Direction.NORTH || newState.getValue(FACING) == Direction.SOUTH)
                                && (neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            newState = newState.setValue(ROTATION, 3);
                        }
                        else if ((newState.getValue(FACING) == Direction.EAST || newState.getValue(FACING) == Direction.WEST)
                                && (neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                            newState = newState.setValue(ROTATION, 3);
                        }
                        //270
                        else if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))) {
                            if (newState.getValue(FACING) == Direction.NORTH) newState = newState.setValue(ROTATION, 2);
                            else if (newState.getValue(FACING) == Direction.SOUTH) newState = newState.setValue(ROTATION, 4);
                        }
                        else if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))
                                && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))) {
                            if (newState.getValue(FACING) == Direction.EAST) newState = newState.setValue(ROTATION, 2);
                            else if (newState.getValue(FACING) == Direction.WEST) newState = newState.setValue(ROTATION, 4);
                        }
                    }
                    else {
                        if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                            if (newState.getValue(FACING) == Direction.UP) newState = newState.setValue(ROTATION, 1);
                            else if (newState.getValue(FACING) == Direction.DOWN) newState = newState.setValue(ROTATION, 3);
                        }
                            if ((neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))
                                    && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))
                                    && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                                newState = newState.setValue(ROTATION, 2);
                            }
                        if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            if (newState.getValue(FACING) == Direction.UP) newState = newState.setValue(ROTATION, 3);
                            else if (newState.getValue(FACING) == Direction.DOWN) newState = newState.setValue(ROTATION, 1);
                        }
                        if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                            && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))
                            && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                            newState = newState.setValue(ROTATION, 4);
                        }
                    }

                    break;
                case 2:
                    newState = newState.setValue(TYPE, 2);
                    if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                            && (neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))) {
                        newState = newState.setValue(TYPE, 1).setValue(ROTATION, 1);
                    }
                    if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                            && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                        newState = newState.setValue(TYPE, 1).setValue(ROTATION, 2);
                    }
                    else if ((neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))
                            && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                        newState = newState.setValue(TYPE, 1).setValue(ROTATION,  state.getValue(FACING).getAxis().isHorizontal() ? 2 : 1);
                    }
                    if (state.getValue(FACING).getAxis().isHorizontal()){
                        //0
                        if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            if (newState.getValue(FACING) == Direction.NORTH) newState = newState.setValue(ROTATION, 4);
                            else if (newState.getValue(FACING) == Direction.SOUTH) newState = newState.setValue(ROTATION, 1);
                        }
                        else if ((neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                            if (newState.getValue(FACING) == Direction.EAST) newState = newState.setValue(ROTATION, 1);
                            else if (newState.getValue(FACING) == Direction.WEST) newState = newState.setValue(ROTATION, 4);
                        }
                        //90
                        else if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            if (newState.getValue(FACING) == Direction.NORTH) newState = newState.setValue(ROTATION, 3);
                            else if (newState.getValue(FACING) == Direction.SOUTH) newState = newState.setValue(ROTATION, 2);
                        }
                        else if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                            if (newState.getValue(FACING) == Direction.EAST) newState = newState.setValue(ROTATION, 2);
                            else if (newState.getValue(FACING) == Direction.WEST) newState = newState.setValue(ROTATION, 3);

                        }
                        //180
                        else if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))) {
                            if (newState.getValue(FACING) == Direction.NORTH) newState = newState.setValue(ROTATION, 2);
                            else if (newState.getValue(FACING) == Direction.SOUTH) newState = newState.setValue(ROTATION, 3);
                        }
                        else if ((neighbors.contains(Direction.DOWN) || diagonalNeighbors.contains(Direction.DOWN))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                            if (newState.getValue(FACING) == Direction.EAST) newState = newState.setValue(ROTATION, 3);
                            else if (newState.getValue(FACING) == Direction.WEST) newState = newState.setValue(ROTATION, 2);
                        }
                        //270
                        else if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))) {
                            if (newState.getValue(FACING) == Direction.NORTH) newState = newState.setValue(ROTATION, 1);
                            else if (newState.getValue(FACING) == Direction.SOUTH) newState = newState.setValue(ROTATION, 4);
                        }
                        else if ((neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))
                                && (neighbors.contains(Direction.UP) || diagonalNeighbors.contains(Direction.UP))) {
                            if (newState.getValue(FACING) == Direction.EAST) newState = newState.setValue(ROTATION, 4);
                            else if (newState.getValue(FACING) == Direction.WEST) newState = newState.setValue(ROTATION, 1);
                        }
                    }
                    else {
                        if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                            if (newState.getValue(FACING) == Direction.UP) newState = newState.setValue(ROTATION, 4);
                            else if (newState.getValue(FACING) == Direction.DOWN) newState = newState.setValue(ROTATION, 3);
                        }
                        if ((neighbors.contains(Direction.EAST) || diagonalNeighbors.contains(Direction.EAST))
                                && (neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))) {
                            if (newState.getValue(FACING) == Direction.UP) newState = newState.setValue(ROTATION, 3);
                            else if (newState.getValue(FACING) == Direction.DOWN) newState = newState.setValue(ROTATION, 4);
                        }
                        if ((neighbors.contains(Direction.SOUTH) || diagonalNeighbors.contains(Direction.SOUTH))
                                && (neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))) {
                            if (newState.getValue(FACING) == Direction.UP) newState = newState.setValue(ROTATION, 2);
                            else if (newState.getValue(FACING) == Direction.DOWN) newState = newState.setValue(ROTATION, 1);
                        }
                        if ((neighbors.contains(Direction.WEST) || diagonalNeighbors.contains(Direction.WEST))
                                && (neighbors.contains(Direction.NORTH) || diagonalNeighbors.contains(Direction.NORTH))) {
                            if (newState.getValue(FACING) == Direction.UP) newState = newState.setValue(ROTATION, 1);
                            else if (newState.getValue(FACING) == Direction.DOWN) newState = newState.setValue(ROTATION, 2);
                        }
                    }


                    break;
                case 1:
                    newState = newState.setValue(TYPE, 1);
                    if (state.getValue(FACING).getAxis().isHorizontal()) newState = newState.setValue(ROTATION, ((neighbors.get(0).getAxis().isVertical())) ? 1 : 2);
                    else if (state.getValue(FACING).getAxis().isVertical()) newState = newState.setValue(ROTATION, ((neighbors.get(0).getAxis() == Direction.Axis.X)) ? 2 : 1);

                    break;
            }
        }
        return newState
                .setValue(RIGHT_UP, rightUp)
                .setValue(LEFT_UP, leftUp)
                .setValue(RIGHT_DOWN, rightDown)
                .setValue(LEFT_DOWN, leftDown);
    }
}
