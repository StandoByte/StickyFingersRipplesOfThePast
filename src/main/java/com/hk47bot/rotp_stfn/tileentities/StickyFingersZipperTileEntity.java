package com.hk47bot.rotp_stfn.tileentities;

import com.github.standobyte.jojo.client.ClientUtil;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.block.ZipperFace;
import com.hk47bot.rotp_stfn.init.InitTileEntities;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.*;

public class StickyFingersZipperTileEntity extends TileEntity implements ITickableTileEntity {
    private final ZipperFace NORTH = new ZipperFace(Direction.NORTH);
    private final ZipperFace SOUTH = new ZipperFace(Direction.SOUTH);
    private final ZipperFace EAST = new ZipperFace(Direction.EAST);
    private final ZipperFace WEST = new ZipperFace(Direction.WEST);
    private final ZipperFace UP = new ZipperFace(Direction.UP);
    private final ZipperFace DOWN = new ZipperFace(Direction.DOWN);
    public ArrayList<ZipperFace> FACES = new ArrayList<>(Arrays.asList(DOWN, UP, NORTH, SOUTH, WEST, EAST));
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
        if (this.level.isClientSide()){
            for (Direction direction : Direction.values()){
                ZipperFace face = FACES.get(direction.get3DDataValue());
                if (face != getFaceValue(direction)){
                    FACES.set(direction.get3DDataValue(), getFaceValue(direction));
                }
            }
        }
    }

    public ZipperFace getFaceValue(Direction direction){
        ZipperFace result = new ZipperFace(direction);
        World world = this.level;
        BlockPos pos = this.getBlockPos();
        BlockState state = world.getBlockState(pos);
        TileEntity entity = world.getBlockEntity(pos);
        if (entity instanceof StickyFingersZipperTileEntity && state.getBlock() instanceof StickyFingersZipperBlock2){
            List<BlockPos> neighbors = new ArrayList<>();

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        BlockPos neighborPos = pos.offset(i, j, k);
                        if (world.getBlockState(neighborPos).getBlock() instanceof StickyFingersZipperBlock2){
                            neighbors.add(neighborPos);
                        }
                    }
                }
            }

            List<Direction> neighborFaceDirections = new ArrayList<>();
            for (BlockPos npos : neighbors){
                BooleanProperty thisDirectionSide = StickyFingersZipperBlock2.DIRECTION_PROPERTIES.get(direction.get3DDataValue());
                BooleanProperty oppositeDirectionSide = StickyFingersZipperBlock2.DIRECTION_PROPERTIES.get(direction.getOpposite().get3DDataValue());
                if (npos == pos){
                    for (BooleanProperty side : StickyFingersZipperBlock2.DIRECTION_PROPERTIES){
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
                else {
                    BlockState nstate = world.getBlockState(npos);
                    for (int i = -1; i < 2; i++) {
                        BlockPos posWithOffset;
                        if (direction == state.getValue(INITIAL_FACING) || direction == state.getValue(INITIAL_FACING).getOpposite()){
                            posWithOffset = pos.relative(state.getValue(INITIAL_FACING), i);
                            if (nstate.getValue(INITIAL_FACING) == state.getValue(INITIAL_FACING)){
                                Direction potentialNeighborDirection = ZipperUtil.relationToNeighbor(posWithOffset, npos);
                                if (potentialNeighborDirection != null
                                        && potentialNeighborDirection != state.getValue(INITIAL_FACING)
                                        && potentialNeighborDirection != state.getValue(INITIAL_FACING).getOpposite()){
                                    if (!neighborFaceDirections.contains(potentialNeighborDirection.getOpposite())){
                                        neighborFaceDirections.add(potentialNeighborDirection.getOpposite());
                                    }
                                }
                            }
                        }
                        else {
                            posWithOffset = pos.relative(direction, i);
                            if (nstate.getValue(INITIAL_FACING) == state.getValue(INITIAL_FACING)){
                                Direction potentialNeighborDirection = ZipperUtil.relationToNeighbor(posWithOffset, npos);
                                if (potentialNeighborDirection != null
                                        && potentialNeighborDirection != direction
                                        && potentialNeighborDirection != direction.getOpposite()){
                                    if (!neighborFaceDirections.contains(potentialNeighborDirection.getOpposite())){
                                        neighborFaceDirections.add(potentialNeighborDirection.getOpposite());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            switch (neighborFaceDirections.size()){
                case 0:
                    result.setType(0);
                    break;
                case 1:
                    result.setType(1);
                    break;
                case 2:
                    if (!neighborFaceDirections.contains(neighborFaceDirections.get(0).getOpposite())){
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
            switch (result.getType()){
                case 4:
                    break;
                case 3:
                    if (direction.getAxis().isHorizontal()){
                        //0
                        if ((direction == Direction.NORTH || direction == Direction.SOUTH)
                                && (neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(3);
                        }
                        else if ((direction == Direction.EAST || direction == Direction.WEST)
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
                        }
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
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
                        }
                        else if ((direction == Direction.EAST || direction == Direction.WEST)
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
                        }
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(4);
                        }
                    }
                    else {
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            result.setRotation(3);
                        }
                        if ((neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            if (direction == Direction.UP) result.setRotation(2);
                            else if (direction == Direction.DOWN) result.setRotation(4);
                        }
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(1);
                        }
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            if (direction == Direction.UP) result.setRotation(4);
                            else if (direction == Direction.DOWN) result.setRotation(2);
                        }
                    }
                    break;
                case 2:
                    if (direction.getAxis().isHorizontal()){
                        //0
                        if ((neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(3);
                        }
                        else if ((neighborFaceDirections.contains(Direction.UP))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(3);
                        }
                        //90
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            result.setRotation(4);
                        }
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            result.setRotation(4);

                        }
                        //180
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.EAST))) {
                            result.setRotation(1);
                        }
                        else if ((neighborFaceDirections.contains(Direction.DOWN))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            result.setRotation(1);
                        }
                        //270
                        else if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(2);
                        }
                        else if ((neighborFaceDirections.contains(Direction.NORTH))
                                && (neighborFaceDirections.contains(Direction.UP))) {
                            result.setRotation(2);
                        }
                    }
                    else {
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            if (direction == Direction.UP) result.setRotation(3);
                            else if (direction == Direction.DOWN) result.setRotation(2);
                        }
                        if ((neighborFaceDirections.contains(Direction.EAST))
                                && (neighborFaceDirections.contains(Direction.SOUTH))) {
                            if (direction == Direction.UP) result.setRotation(4);
                            else if (direction == Direction.DOWN) result.setRotation(1);
                        }
                        if ((neighborFaceDirections.contains(Direction.SOUTH))
                                && (neighborFaceDirections.contains(Direction.WEST))) {
                            if (direction == Direction.UP) result.setRotation(1);
                            else if (direction == Direction.DOWN) result.setRotation(4);
                        }
                        if ((neighborFaceDirections.contains(Direction.WEST))
                                && (neighborFaceDirections.contains(Direction.NORTH))) {
                            if (direction == Direction.UP) result.setRotation(2);
                            else if (direction == Direction.DOWN) result.setRotation(3);
                        }
                    }
                    break;
                case 1:
                    if (direction.getAxis().isHorizontal()) result.setRotation(((neighborFaceDirections.get(0).getAxis().isVertical())) ? 2 : 1);
                    else if (direction.getAxis().isVertical()) result.setRotation(((neighborFaceDirections.get(0).getAxis() == Direction.Axis.X)) ? 1 : 2);
                    break;
            }
//            result.setRightUp(true);
//            result.setRightDown(true);
//            result.setLeftDown(true);
//            result.setLeftUp(true);

            if (direction.getAxis().isHorizontal()){
                //0
                if ((neighborFaceDirections.contains(Direction.UP))
                        && (neighborFaceDirections.contains(Direction.WEST))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.west();
                    if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setRightUp(true);
                    }
                }
                else if ((neighborFaceDirections.contains(Direction.UP))
                        && (neighborFaceDirections.contains(Direction.SOUTH))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.south();
                    if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setRightUp(true);
                    }
                }
                //90
                if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.WEST))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.west();
                   if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setRightDown(true);
                    }
                }
                else if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.SOUTH))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.south();
                   if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setRightDown(true);
                    }

                }
                //180
                if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.EAST))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.east();
                   if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setLeftDown(true);
                    }
                }
                else if ((neighborFaceDirections.contains(Direction.DOWN))
                        && (neighborFaceDirections.contains(Direction.NORTH))) {
                    BlockPos neighborPos1 = pos.below();
                    BlockPos neighborPos2 = pos.north();
                   if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setLeftDown(true);
                    }
                }
                //270
                if ((neighborFaceDirections.contains(Direction.EAST))
                        && (neighborFaceDirections.contains(Direction.UP))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.east();
                   if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setLeftUp(true);
                    }
                }
                else if ((neighborFaceDirections.contains(Direction.NORTH))
                        && (neighborFaceDirections.contains(Direction.UP))) {
                    BlockPos neighborPos1 = pos.above();
                    BlockPos neighborPos2 = pos.north();
                   if ((world.getBlockState(neighborPos1).getBlock() instanceof StickyFingersZipperBlock2)
                            && (world.getBlockState(neighborPos2).getBlock() instanceof StickyFingersZipperBlock2)){
                        result.setLeftUp(true);
                    }
                }
            }
//            else {
//                if ((neighborFaceDirections.contains(Direction.EAST))
//                        && (neighborFaceDirections.contains(Direction.NORTH))) {
//                    if (direction == Direction.UP) result.setRotation(3);
//                    else if (direction == Direction.DOWN) result.setRotation(2);
//                }
//                if ((neighborFaceDirections.contains(Direction.EAST))
//                        && (neighborFaceDirections.contains(Direction.SOUTH))) {
//                    if (direction == Direction.UP) result.setRotation(4);
//                    else if (direction == Direction.DOWN) result.setRotation(1);
//                }
//                if ((neighborFaceDirections.contains(Direction.SOUTH))
//                        && (neighborFaceDirections.contains(Direction.WEST))) {
//                    if (direction == Direction.UP) result.setRotation(1);
//                    else if (direction == Direction.DOWN) result.setRotation(4);
//                }
//                if ((neighborFaceDirections.contains(Direction.WEST))
//                        && (neighborFaceDirections.contains(Direction.NORTH))) {
//                    if (direction == Direction.UP) result.setRotation(2);
//                    else if (direction == Direction.DOWN) result.setRotation(3);
//                }
//            }
//            RotpStickyFingersAddon.getLogger().info("{} RU {} RD {} LU {} LD {} at X {} Y {} Z {}", neighborFaceDirections, result.isRightUp(), result.isRightDown(), result.isLeftUp(), result.isLeftDown(), pos.getX(), pos.getY(), pos.getZ());
        }
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getFaceTexture(ZipperFace face){
        BlockState state = this.getBlockState();
        String type = "";
        String rotation = "";
        switch (face.getType()){
            case 0:
                type = "diagonal";
                break;
            case 1:
                type = "straight";
                break;
            case 2:
                type = "corner";
                break;
            case 3:
                type = "t";
                break;
            case 4:
                type = "cross";
                break;
        }
        switch (face.getRotation()){
            case 1:
                if (face.getType() == 1) rotation = "horizontal";
                else rotation = "1";
                break;
            case 2:
                if (face.getType() == 1) rotation = "vertical";
                else rotation = "2";
                break;
            case 3:
                rotation = "3";
                break;
            case 4:
                rotation = "4";
                break;
        }
        return new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/zipper/" + type + "/" + "zipper_" + type + (!type.equals("cross") && !type.equals("diagonal") ? "_" +rotation : "")+(state.getValue(OPEN) ? "_open" : "") + ".png");

    }
}
