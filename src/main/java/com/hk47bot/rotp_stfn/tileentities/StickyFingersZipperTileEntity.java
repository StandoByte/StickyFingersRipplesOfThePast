package com.hk47bot.rotp_stfn.tileentities;

import com.github.standobyte.jojo.client.ClientUtil;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.block.ZipperFace;
import com.hk47bot.rotp_stfn.init.InitTileEntities;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.INITIAL_FACING;
import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.OPEN;

public class StickyFingersZipperTileEntity extends TileEntity implements ITickableTileEntity {
    private final ZipperFace NORTH = new ZipperFace(Direction.NORTH);
    private final ZipperFace SOUTH = new ZipperFace(Direction.SOUTH);
    private final ZipperFace EAST = new ZipperFace(Direction.EAST);
    private final ZipperFace WEST = new ZipperFace(Direction.WEST);
    private final ZipperFace UP = new ZipperFace(Direction.UP);
    private final ZipperFace DOWN = new ZipperFace(Direction.DOWN);
    public ArrayList<ZipperFace> FACES = new ArrayList<>(Arrays.asList(DOWN, UP, NORTH, SOUTH, WEST, EAST));
    public int overlayTickCount = 0;
    private int autoCloseTimer = 0;
    private static final int AUTO_CLOSE_DELAY_TICKS = 3000;

    private UUID ownerUUID;
    private static final int MAX_DISTANCE_SQUARED = 5 * 64; // 64 bla bla bla blocks

    public StickyFingersZipperTileEntity(TileEntityType<?> p_i48283_1_) {
        super(p_i48283_1_);
    }

    public StickyFingersZipperTileEntity() {
        this(InitTileEntities.ZIPPER_TILE_ENTITY.get());
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderFace(Direction direction) {
        BlockState state = this.getBlockState();
        BooleanProperty thisDirectionSide = StickyFingersZipperBlock2.DIRECTION_PROPERTIES.get(direction.get3DDataValue());
        return (ClientUtil.canSeeStands() && state.getValue(thisDirectionSide));
    }


    @Override
    public void tick() {
        if (this.level == null) return;

        if (this.level.isClientSide()) {
            overlayTickCount++;
            for (Direction direction : Direction.values()) {
                ZipperFace face = FACES.get(direction.get3DDataValue());
                if (face != getFaceValue(direction)) {
                    FACES.set(direction.get3DDataValue(), getFaceValue(direction));
                }
            }
            return;
        }

        BlockState state = this.getBlockState();
        if (state.getValue(OPEN)) {
            boolean shouldClose = false;

            this.autoCloseTimer++;
            if (this.autoCloseTimer >= AUTO_CLOSE_DELAY_TICKS) {
                shouldClose = true;
            }

            if (this.ownerUUID != null) {
                PlayerEntity owner = this.level.getPlayerByUUID(this.ownerUUID);
                if (owner == null || owner.isDeadOrDying()) {
                    this.getBlockState().getBlock().destroy(this.level, this.getBlockPos(), this.getBlockState());
                }
                else if (owner.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) > MAX_DISTANCE_SQUARED) {
                    shouldClose = true;
                }

            }

            if (shouldClose) {
                this.level.setBlock(this.getBlockPos(), state.setValue(OPEN, false), 3);
                BlockPos linkedPos = StickyFingersZipperBlock2.getLinkedZipperBlockPos(state, this.getBlockPos(), this.level);
                if (this.level.getBlockState(linkedPos).getBlock() instanceof StickyFingersZipperBlock2){
                    this.level.setBlock(linkedPos, this.level.getBlockState(linkedPos).setValue(OPEN, false), 3);
                }
                this.autoCloseTimer = 0;
            }
        } else {
            if (this.autoCloseTimer > 0) {
                this.autoCloseTimer = 0;
            }
        }
    }

//    private List<BlockPos> getNeighborZipperBlocksAndSelf(World world, BlockPos pos){
//        List<BlockPos> neighbors = new ArrayList<>();
//        for (int x = -1; x < 2; x++) {
//            for (int y = -1; y < 2; y++) {
//                for (int z = -1; z < 2; z++) {
//                    BlockPos neighborPos = pos.offset(x, y, z);
//                    if (world.getBlockState(neighborPos).getBlock() instanceof StickyFingersZipperBlock2){
//                        neighbors.add(neighborPos);
//                    }
//                }
//            }
//        }
//        return neighbors;
//    }

    private void addNeighborDirectionsOfSidesToList(List<Direction> neighborFaceDirections, Direction direction, BlockState state) {
        BooleanProperty thisDirectionSide = StickyFingersZipperBlock2.DIRECTION_PROPERTIES.get(direction.get3DDataValue());
        BooleanProperty oppositeDirectionSide = StickyFingersZipperBlock2.DIRECTION_PROPERTIES.get(direction.getOpposite().get3DDataValue());
        for (BooleanProperty side : StickyFingersZipperBlock2.DIRECTION_PROPERTIES) {
            if (side != thisDirectionSide && side != oppositeDirectionSide && state.getValue(side)) {
                Direction direction1 = Direction.from3DDataValue(StickyFingersZipperBlock2.DIRECTION_PROPERTIES.indexOf(side));
                if (direction1 != state.getValue(INITIAL_FACING).getOpposite()) {
                    if (!neighborFaceDirections.contains(direction1.getOpposite())) {
                        neighborFaceDirections.add(direction1.getOpposite());
                    }
                }
            }
        }
    }

    private void addNeighborDirectionsOfBlocksToList(List<Direction> neighborFaceDirections, World world, BlockPos pos, Direction direction, BlockState state) {
        for (Direction offset : ZipperUtil.getDirectionListByAxis(direction.getAxis())) {
            for (int i = -1; i < 2; i++) {
                BlockPos posWithOffset = pos.relative(direction, i).relative(offset);
                BlockState nstate = world.getBlockState(posWithOffset);
                if (nstate.getBlock() instanceof StickyFingersZipperBlock2
                        && nstate.getValue(INITIAL_FACING) == state.getValue(INITIAL_FACING)
                        && (i != 0 || nstate.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction)) == state.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction)))) {
                    if (offset != direction
                            && offset != direction.getOpposite()
                            && !neighborFaceDirections.contains(offset)) {
                        neighborFaceDirections.add(offset);
                    }
                }
            }
        }
    }

    public ZipperFace getFaceValue(Direction direction) {
        ZipperFace result = new ZipperFace(direction);
        World world = this.level;
        BlockPos pos = this.getBlockPos();
        BlockState state = world.getBlockState(pos);
        TileEntity entity = world.getBlockEntity(pos);
        if (entity instanceof StickyFingersZipperTileEntity && state.getBlock() instanceof StickyFingersZipperBlock2) {
            List<Direction> neighborFaceDirections = new ArrayList<>();
            addNeighborDirectionsOfSidesToList(neighborFaceDirections, direction, state);
            addNeighborDirectionsOfBlocksToList(neighborFaceDirections, world, pos, direction, state);
            result.neighbours = neighborFaceDirections;
            switch (neighborFaceDirections.size()) {
                case 0:
                    result.setType(0);
                    break;
                case 1:
                    result.setType(1);
                    break;
                case 2:
                    if (!neighborFaceDirections.contains(neighborFaceDirections.get(0).getOpposite())) {
                        result.setType(2);
                    }
                    break;
                case 3:
                    result.setType(3);
                    break;
                default:
                    result.setType(4);
                    break;
            }
            switch (result.getType()) {
                case 4:
                    break;
                case 3:
                    if (direction.getAxis().isHorizontal()) {
                        //0
                        if ((direction == Direction.NORTH || direction == Direction.SOUTH)
                                && (neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(3);
                        } else if ((direction == Direction.EAST || direction == Direction.WEST)
                                && (neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.NORTH))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(3);
                        }
                        //90
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(4);
                        } else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.NORTH))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(2);
                        }
                        //180
                        else if ((direction == Direction.NORTH || direction == Direction.SOUTH)
                                && (neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(1);
                        } else if ((direction == Direction.EAST || direction == Direction.WEST)
                                && (neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.NORTH))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(1);
                        }
                        //270
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(2);
                        } else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(4);
                        }
                    } else {
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            result.setRotation(1);
                        }
                        if ((neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            if (direction == Direction.UP) result.setRotation(4);
                            else if (direction == Direction.DOWN) result.setRotation(4);
                        }
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(3);
                        }
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            if (direction == Direction.UP) result.setRotation(2);
                            else if (direction == Direction.DOWN) result.setRotation(2);
                        }
                    }
                    break;
                case 2:
                    if (direction.getAxis().isHorizontal()) {
                        //0
                        if ((neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(3);
                        } else if ((neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(3);
                        }
                        //90
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(4);
                        } else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(4);

                        }
                        //180
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.EAST))) {
                            result.setRotation(1);
                        } else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            result.setRotation(1);
                        }
                        //270
                        else if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(2);
                        } else if ((neighborFaceDirections.contains(Direction.NORTH))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(2);
                        }
                    } else {
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            result.setRotation(1);
                        }
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(2);
                        }
                        if ((neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(3);
                        }
                        if ((neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            result.setRotation(4);
                        }
                    }
                    break;
                case 1:
                    if (direction.getAxis().isHorizontal())
                        result.setRotation(((neighborFaceDirections.get(0).getAxis().isVertical())) ? 2 : 1);
                    else if (direction.getAxis().isVertical())
                        result.setRotation(((neighborFaceDirections.get(0).getAxis() == Direction.Axis.X)) ? 1 : 2);
                    break;
            }
            if (direction.getAxis().isHorizontal()) {
                //0
                if ((neighborFaceDirections.contains(Direction.UP))
                        && (neighborFaceDirections.contains(Direction.WEST))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.west();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.above().west(), result.getDirection())) {
                        result.setRightUp(true);
                    }
                } else if ((neighborFaceDirections.contains(Direction.UP))
                        && (neighborFaceDirections.contains(Direction.SOUTH))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.south();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.above().south(), result.getDirection())) {
                        result.setRightUp(true);
                    }
                }
                //90
                if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.WEST))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.west();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.below().west(), result.getDirection())) {
                        result.setRightDown(true);
                    }
                } else if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.SOUTH))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.south();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.below().south(), result.getDirection())) {
                        result.setRightDown(true);
                    }

                }
                //180
                if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.EAST))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.east();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.below().east(), result.getDirection())) {
                        result.setLeftDown(true);
                    }
                } else if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.NORTH))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.north();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.below().north(), result.getDirection())) {
                        result.setLeftDown(true);
                    }
                }
                //270
                if ((neighborFaceDirections.contains(Direction.EAST))
                        && (neighborFaceDirections.contains(Direction.UP))
                        && !(world.getBlockState(pos.above().east()).getBlock() instanceof StickyFingersZipperBlock2)) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.east();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.above().east(), result.getDirection())) {
                        result.setLeftUp(true);
                    }
                } else if ((neighborFaceDirections.contains(Direction.NORTH))
                        && (neighborFaceDirections.contains(Direction.UP))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.north();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.above().north(), result.getDirection())) {
                        result.setLeftUp(true);
                    }
                }
            } else {
                if ((neighborFaceDirections.contains(Direction.EAST))
                        && (neighborFaceDirections.contains(Direction.NORTH))) {
                    BlockPos neighborPos1 = pos.east();
                    BlockPos neighborPos2 = pos.north();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.east().north(), result.getDirection())) {
                        result.setRightDown(true);
                    }
                }
                if ((neighborFaceDirections.contains(Direction.EAST))
                        && (neighborFaceDirections.contains(Direction.SOUTH))) {
                    BlockPos neighborPos1 = pos.east();
                    BlockPos neighborPos2 = pos.south();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.east().south(), result.getDirection())) {
                        result.setRightUp(true);
                    }
                }
                if ((neighborFaceDirections.contains(Direction.SOUTH))
                        && (neighborFaceDirections.contains(Direction.WEST))) {
                    BlockPos neighborPos1 = pos.west();
                    BlockPos neighborPos2 = pos.south();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.west().south(), result.getDirection())) {
                        result.setLeftUp(true);
                    }
                }
                if ((neighborFaceDirections.contains(Direction.WEST))
                        && (neighborFaceDirections.contains(Direction.NORTH))) {
                    BlockPos neighborPos1 = pos.west();
                    BlockPos neighborPos2 = pos.north();
                    if (shouldAddCorner(world, neighborPos1, neighborPos2, pos.west().north(), result.getDirection())) {
                        result.setLeftDown(true);
                    }
                }
            }
        }
        return result;
    }

    public boolean shouldAddCorner(World world, BlockPos pos1, BlockPos pos2, BlockPos pos3, Direction facing) {
        return world.getBlockState(pos1).getBlock() instanceof StickyFingersZipperBlock2
                && world.getBlockState(pos2).getBlock() instanceof StickyFingersZipperBlock2
                && !((world.getBlockState(pos3).getBlock() instanceof StickyFingersZipperBlock2)
                || (world.getBlockState(pos3.relative(facing)).getBlock() instanceof StickyFingersZipperBlock2)
                || (world.getBlockState(pos3.relative(facing.getOpposite())).getBlock() instanceof StickyFingersZipperBlock2));


    }

    public void setOwner(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        this.setChanged();
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (this.ownerUUID != null) {
            compound.putUUID("owner", this.ownerUUID);
        }
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        if (compound.hasUUID("owner")) {
            this.ownerUUID = compound.getUUID("owner");
        }
    }
}
