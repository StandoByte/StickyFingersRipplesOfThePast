package com.hk47bot.rotp_stfn.block;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.action.stand.StickyFingersPlaceZipper;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import com.hk47bot.rotp_stfn.init.InitSounds;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import net.minecraft.block.*;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static com.hk47bot.rotp_stfn.action.stand.StickyFingersPlaceZipper.isBlockFree;

public class StickyFingersZipperBlock2 extends SixWayBlock implements IWaterLoggable, ITileEntityProvider {
    public static final DirectionProperty INITIAL_FACING = BlockStateProperties.FACING;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static ArrayList<BooleanProperty> DIRECTION_PROPERTIES = new ArrayList<>();

    public StickyFingersZipperBlock2(Properties properties) {
        super(1, properties);
        DIRECTION_PROPERTIES.add(DOWN);
        DIRECTION_PROPERTIES.add(UP);
        DIRECTION_PROPERTIES.add(NORTH);
        DIRECTION_PROPERTIES.add(SOUTH);
        DIRECTION_PROPERTIES.add(WEST);
        DIRECTION_PROPERTIES.add(EAST);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(OPEN, Boolean.FALSE)
                .setValue(INITIAL_FACING, Direction.NORTH)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(NORTH, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE)
        );
    }
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add((OPEN));
        builder.add((INITIAL_FACING));
        builder.add((WATERLOGGED));
        //NORTH
        builder.add((NORTH));
        //SOUTH
        builder.add((SOUTH));
        //EAST
        builder.add((EAST));
        //WEST
        builder.add((WEST));
        //UP
        builder.add((UP));
        //DOWN
        builder.add((DOWN));
    }
    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new StickyFingersZipperTileEntity();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return state.getValue(OPEN) ? VoxelShapes.empty() : super.getCollisionShape(state, world, pos, context);
    }
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape shape = VoxelShapes.empty();
        if (state.getValue(NORTH)){
            shape = VoxelShapes.join(shape, VoxelShapes.box(1, 1, 1, 0, 0, 0.99), IBooleanFunction.OR);
        }
        if (state.getValue(SOUTH)){
            shape = VoxelShapes.join(shape, VoxelShapes.box(1, 1, 0, 0, 0, 0.01), IBooleanFunction.OR);
        }
        if (state.getValue(EAST)){
            shape = VoxelShapes.join(shape, VoxelShapes.box(0, 1, 1, 0.01, 0, 0), IBooleanFunction.OR);
        }
        if (state.getValue(WEST)){
            shape = VoxelShapes.join(shape, VoxelShapes.box(1, 1, 1, 0.99, 0, 0), IBooleanFunction.OR);
        }
        if (state.getValue(UP)){
            shape = VoxelShapes.join(shape, VoxelShapes.box(1, 0, 1, 0, 0.01, 0), IBooleanFunction.OR);
        }
        if (state.getValue(DOWN)){
            shape = VoxelShapes.join(shape, VoxelShapes.box(1, 1, 1, 0, 0.99, 0), IBooleanFunction.OR);
        }
        return shape;
    }

    public static void placeZippers(World world, BlockPos targetedBlockPos, Direction direction) {
        world.setBlockAndUpdate(targetedBlockPos.relative(direction), getZipperState(world, targetedBlockPos.relative(direction), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(INITIAL_FACING, direction)));
        if (!isBlockFree(world, targetedBlockPos.relative(direction.getOpposite()))){
            world.setBlockAndUpdate(targetedBlockPos.relative(direction.getOpposite(), 2), getZipperState(world, targetedBlockPos.relative(direction.getOpposite(), 2), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(INITIAL_FACING, direction.getOpposite())));
        }
        else {
            world.setBlockAndUpdate(targetedBlockPos.relative(direction.getOpposite()), getZipperState(world, targetedBlockPos.relative(direction.getOpposite()), InitBlocks.STICKY_FINGERS_ZIPPER.get().defaultBlockState().setValue(INITIAL_FACING, direction.getOpposite())));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.relative(state.getValue(INITIAL_FACING).getOpposite())).isFaceSturdy(world, pos.relative(state.getValue(INITIAL_FACING).getOpposite()), state.getValue(INITIAL_FACING));
    }
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
        if (!canSurvive(state, world, pos)){
            destroy(world, pos, state);
            return Blocks.AIR.defaultBlockState();
        }
        BlockState zipperState = getZipperState((World) world, pos, state);
        if (neighborState.getBlock() instanceof StickyFingersZipperBlock2){
            if (neighborState.getValue(INITIAL_FACING) == state.getValue(INITIAL_FACING)){
                zipperState = getZipperState((World) world, pos, state).setValue(OPEN, neighborState.getValue(OPEN));
            }
        }
        return zipperState;
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        BlockPos linkedPos = getLinkedBlockPos(state, pos, world);
        BlockState linkedState = world.getBlockState(linkedPos);
        if (linkedState.is(this)) {
            world.destroyBlock(linkedPos, true);
        }
        world.destroyBlock(pos, true);
    }

    public static BlockPos getLinkedBlockPos(BlockState state, BlockPos pos, IWorld world){
        if (state.getBlock() instanceof StickyFingersZipperBlock2){
            return pos.relative(state.getValue(INITIAL_FACING).getOpposite(), StickyFingersPlaceZipper.isBlockZipper((World) world, pos.relative(state.getValue(INITIAL_FACING).getOpposite(), 2)) ? 2 : 3);
        }
        return pos;
    }

    public static BlockState getZipperState(World world, BlockPos pos, BlockState state) {
        BlockState newState = state;
        List<Direction> neighbors = new ArrayList<>();
        List<Direction> diagonalNeighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof StickyFingersZipperBlock2) {
                if (state.getValue(INITIAL_FACING).getAxis() != direction.getAxis()
                        && neighborState.getValue(INITIAL_FACING).getAxis() == state.getValue(INITIAL_FACING).getAxis()) {
                    neighbors.add(direction);
                }
            }
        }
        List<Direction> stateFacing = new ArrayList<>();
        stateFacing.add(state.getValue(INITIAL_FACING));
        for (Direction direction : stateFacing){
            BlockPos offsetPos = pos.relative(direction);
            for (Direction direction1 : Direction.values()) {
                BlockPos neighborPos = offsetPos.relative(direction1);
                BlockState neighborState = world.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof StickyFingersZipperBlock2) {
                    if (state.getValue(INITIAL_FACING).getAxis() != direction1.getAxis()
                            && neighborState.getValue(INITIAL_FACING) == state.getValue(INITIAL_FACING)) {
                        diagonalNeighbors.add(direction1);
                    }
                }
            }
        }
        for (Direction direction : Direction.values()){
            newState = newState.setValue(DIRECTION_PROPERTIES.get(direction.get3DDataValue()), false);
            if ((diagonalNeighbors.contains(direction.getOpposite()) && newState.getValue(INITIAL_FACING).getAxis().isHorizontal())
                    || (direction == newState.getValue(INITIAL_FACING))){
                newState = newState.setValue(DIRECTION_PROPERTIES.get(direction.get3DDataValue()), true);
            }
            else if (((diagonalNeighbors.contains(direction) && newState.getValue(INITIAL_FACING).getAxis().isVertical()))){
                newState = newState.setValue(DIRECTION_PROPERTIES.get(direction.getOpposite().get3DDataValue()), true);
            }
        }
        return newState;
    }

}
