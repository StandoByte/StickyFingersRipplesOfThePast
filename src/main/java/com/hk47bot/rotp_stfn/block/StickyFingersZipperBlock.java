package com.hk47bot.rotp_stfn.block;

import com.hk47bot.rotp_stfn.capability.EntityZipperDataProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class StickyFingersZipperBlock extends Block {
    public static final DirectionProperty DIRECTION = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 1, 4);
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 4);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    protected static final VoxelShape WALL_NORTH_SHAPE = Block.box(4.0D, 4.0D, 15.0D, 12.0D, 12.0D, 16.0D);
    protected static final VoxelShape WALL_SOUTH_SHAPE = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 1.0D);
    protected static final VoxelShape WALL_WEST_SHAPE = Block.box(15.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
    protected static final VoxelShape WALL_EAST_SHAPE = Block.box(0.0D, 4.0D, 4.0D, 1.0D, 12.0D, 12.0D);
    protected static final VoxelShape FLOOR_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D);
    protected static final VoxelShape CEILING_SHAPE = Block.box(4.0D, 15.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public StickyFingersZipperBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DIRECTION, Direction.NORTH)
                .setValue(ROTATION, 1)
                .setValue(TYPE, 1)
                .setValue(OPEN, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch(state.getValue(DIRECTION)) {
            case UP:
                return CEILING_SHAPE;
            case DOWN:
                return FLOOR_SHAPE;
            case WEST:
                return WALL_WEST_SHAPE;
            case EAST:
                return WALL_EAST_SHAPE;
            case NORTH:
                return WALL_NORTH_SHAPE;
            case SOUTH:
                return WALL_SOUTH_SHAPE;
        }
        return CEILING_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
        builder.add(ROTATION);
        builder.add(TYPE);
        builder.add(OPEN);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.getCapability(EntityZipperDataProvider.CAPABILITY).ifPresent(zipperCap -> {
            if (zipperCap.getRemainingZipperCooldown() == 0 && (state.getValue(DIRECTION) == zipperCap.getEnterDirection() || zipperCap.getEnterDirection() == null)){
                zipperCap.setRemainingZipperTicks(20);
                zipperCap.setEnterDirection(state.getValue(DIRECTION));
            }
            else if (zipperCap.getEnterDirection() != null && state.getValue(DIRECTION) == zipperCap.getEnterDirection().getOpposite()){
                zipperCap.setRemainingZipperCooldown(40);
                zipperCap.setEnterDirection(null);
            }
        });
    }
}
