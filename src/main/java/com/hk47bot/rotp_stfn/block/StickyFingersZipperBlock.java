package com.hk47bot.rotp_stfn.block;

import com.hk47bot.rotp_stfn.capability.EntityZipperDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class StickyFingersZipperBlock extends Block implements IWaterLoggable {
    public static final DirectionProperty DIRECTION = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 1, 4);
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 4);
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

//    @Override
//    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
//        return VoxelShapes.empty();
//    }

//        @Override
//    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
//        entity.getCapability(EntityZipperDataProvider.CAPABILITY).ifPresent(zipperCap -> {
//            if (zipperCap.getRemainingZipperCooldown() == 0 && (state.getValue(DIRECTION) == zipperCap.getEnterDirection() || zipperCap.getEnterDirection() == null)){
//                zipperCap.setRemainingZipperTicks(20);
//                zipperCap.setEnterDirection(state.getValue(DIRECTION));
//            }
//            else if (zipperCap.getEnterDirection() != null && state.getValue(DIRECTION) == zipperCap.getEnterDirection().getOpposite()){
//                zipperCap.setRemainingZipperCooldown(40);
//                zipperCap.setEnterDirection(null);
//            }
//        });
//    }

}
