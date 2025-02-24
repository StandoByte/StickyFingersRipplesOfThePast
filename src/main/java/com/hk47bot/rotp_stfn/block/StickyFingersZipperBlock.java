package com.hk47bot.rotp_stfn.block;

import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


public class StickyFingersZipperBlock extends Block implements IWaterLoggable {
    public static final DirectionProperty DIRECTION = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 1, 4);
    // 1 - 0 гр, 2 - 90 гр, 3 - 180 гр, 4 - 270 гр
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 4);
    // 1 - прямо, 2 - г, 3 - т, 4 - х
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public StickyFingersZipperBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(DIRECTION, Direction.NORTH)
                .setValue(ROTATION, 1)
                .setValue(TYPE, 1)
                .setValue(OPEN, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
        builder.add(WATERLOGGED);
        builder.add(ROTATION);
        builder.add(TYPE);
        builder.add(OPEN);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(DIRECTION)){
            default:
                return VoxelShapes.box(1, 1, 1, 0, 0, 0.99);
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
        }

    }
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos blockPos, boolean p_220069_6_) {
        List<BlockPos> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()){
            BlockPos neighbourPos = ZipperUtil.getBlockInDirection(direction, pos);
            if (world.getBlockState(neighbourPos).getBlock() instanceof StickyFingersZipperBlock){
                neighbours.add(neighbourPos);
            }
        }
        if (!neighbours.isEmpty()){
            if (state.getValue(TYPE) == 1 && (neighbours.contains(pos.north()) || neighbours.contains(pos.south()) || neighbours.contains(pos.east()) || neighbours.contains(pos.west()))){
                world.setBlockAndUpdate(pos, state.setValue(ROTATION, 2));
            }
            if (isXForm(neighbours, pos)){
                world.setBlockAndUpdate(pos, state.setValue(TYPE, 4));
            }
            else if (isTForm(neighbours, pos)){
                world.setBlockAndUpdate(pos, state.setValue(TYPE, 3));
            }
            else if (isCorner(neighbours, pos)) {
                world.setBlockAndUpdate(pos, state.setValue(TYPE, 2));
            }
        }
    }

    private boolean isCorner(List<BlockPos> neighbours, BlockPos pos) {
        return (neighbours.contains(pos.above()) && neighbours.contains(pos.north())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.south())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.east())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.west())) ||
                (neighbours.contains(pos.below()) && neighbours.contains(pos.north())) ||
                (neighbours.contains(pos.below()) && neighbours.contains(pos.south())) ||
                (neighbours.contains(pos.below()) && neighbours.contains(pos.east())) ||
                (neighbours.contains(pos.below()) && neighbours.contains(pos.west())) ||
                (neighbours.contains(pos.north()) && neighbours.contains(pos.west())) ||
                (neighbours.contains(pos.north()) && neighbours.contains(pos.east())) ||
                (neighbours.contains(pos.south()) && neighbours.contains(pos.west())) ||
                (neighbours.contains(pos.south()) && neighbours.contains(pos.east()));
    }

    private boolean isTForm(List<BlockPos> neighbours, BlockPos pos){
        return (neighbours.contains(pos.above()) && neighbours.contains(pos.north()) && neighbours.contains(pos.south())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.east()) && neighbours.contains(pos.west())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.north()) && neighbours.contains(pos.below())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.south()) && neighbours.contains(pos.below())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.east()) && neighbours.contains(pos.below())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.west()) && neighbours.contains(pos.below())) ||
                (neighbours.contains(pos.below()) && neighbours.contains(pos.north()) && neighbours.contains(pos.south())) ||
                (neighbours.contains(pos.below()) && neighbours.contains(pos.east()) && neighbours.contains(pos.west()));
    }

    private boolean isXForm(List<BlockPos> neighbours, BlockPos pos){
        return (neighbours.contains(pos.above()) && neighbours.contains(pos.north()) && neighbours.contains(pos.south()) && neighbours.contains(pos.below())) ||
                (neighbours.contains(pos.above()) && neighbours.contains(pos.east()) && neighbours.contains(pos.west()) && neighbours.contains(pos.below()));
    }

}
