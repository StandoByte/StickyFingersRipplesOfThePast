package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.capability.entity.PlayerUtilCap;
import com.github.standobyte.jojo.capability.entity.PlayerUtilCapProvider;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.hk47bot.rotp_stfn.util.ZipperUtil.hasZippersAround;

@Mixin(value = AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow protected abstract BlockState asState();

    @Inject(method = "updateNeighbourShapes(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;II)V", at = @At("HEAD"), cancellable = true)
    private void iHateFuckingFinal(IWorld world, BlockPos pos, int p_241482_3_, int p_241482_4_, CallbackInfo ci){
        if (this.getBlock() instanceof StickyFingersZipperBlock){
            BlockState state = world.getBlockState(pos);
            for (int i = -1; i < 2; i+=1) {
                for (int j = -1; j < 2; j+=1) {
                    for (int k = -1; k < 2; k+=1) {
                        BlockPos neighbourPos = pos.offset(i, j, k);
                        if (world.getBlockState(neighbourPos).getBlock() instanceof StickyFingersZipperBlock
                                && world.getBlockState(neighbourPos).getValue(StickyFingersZipperBlock.INITIAL_FACING) == state.getValue(StickyFingersZipperBlock.INITIAL_FACING)){
                            BlockState blockstate = world.getBlockState(neighbourPos);
                            BlockState blockstate1 = blockstate.updateShape(Direction.DOWN, this.asState(), world, neighbourPos, pos);
                            Block.updateOrDestroy(blockstate, blockstate1, world, neighbourPos, p_241482_3_, p_241482_4_);
                        }
                    }
                }
            }
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;", cancellable = true)
    private void phaseThroughBlocks(IBlockReader world, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> info) {
        VoxelShape blockShape = getBlock().getCollisionShape(this.asState(), world, pos, context);
        try {
            if (!blockShape.isEmpty() && context instanceof EntitySelectionContext) {
                Entity entity = context.getEntity();
                if (getBlock() instanceof StickyFingersZipperBlock
                        || asState().getDestroySpeed(world, pos) < 0
                        || isBlockVanillaDoor(world, pos)){
                    info.setReturnValue(blockShape);
                }
                else if (hasZippersAround(pos, world)){
                    info.setReturnValue(createCollisionAdaptingShape(world, pos));
                }
                else if (entity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) entity;
                    boolean isAbove = isAbove(entity, blockShape, pos, false);
                    boolean doubleShift = player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(
                            PlayerUtilCap::getDoubleShiftPress).orElse(false);
                    player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                        if (cap.isInGround() && (!isAbove || doubleShift)
                                && IStandPower.getPlayerStandPower(player).getType() == InitStands.STAND_STICKY_FINGERS.getStandType()
                                && IStandPower.getPlayerStandPower(player).getResolveLevel() == IStandPower.getPlayerStandPower(player).getMaxResolveLevel()) {
                            info.setReturnValue(createCollisionAdaptingShape(world, pos));
                        }
                    });
                }
                else {
                    info.setReturnValue(blockShape);
                }
            }
        }
        catch (Exception e){
            info.setReturnValue(blockShape);
        }
    }

    @Unique
    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos, boolean defaultValue) {
        return entity.getY() > (double)pos.getY() + shape.bounds().max(Direction.Axis.Y) - (entity.isOnGround() ? 8.05/16.0 : 0.0015);
    }

    @Unique
    private VoxelShape createCollisionAdaptingShape(IBlockReader world, BlockPos pos){
        VoxelShape shape = VoxelShapes.empty();

        if (ZipperUtil.isBlockFree(world, pos.above()) || isBlockUnpassable(world, pos, Direction.UP)) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0.0, 1, 0.0, 1, 0.99, 1), IBooleanFunction.OR);
        }
        if (ZipperUtil.isBlockFree(world, pos.below()) || isBlockUnpassable(world, pos, Direction.DOWN)) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 0.01, 1), IBooleanFunction.OR);
        }
        if (ZipperUtil.isBlockFree(world, pos.north()) || isBlockUnpassable(world, pos, Direction.NORTH)) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 1, 0.01), IBooleanFunction.OR);
        }
        if (ZipperUtil.isBlockFree(world, pos.south()) || isBlockUnpassable(world, pos, Direction.SOUTH)) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 1, 1, 1, 0.99), IBooleanFunction.OR);
        }
        if (ZipperUtil.isBlockFree(world, pos.west()) || isBlockUnpassable(world, pos, Direction.WEST)) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 0.01, 1, 1), IBooleanFunction.OR);
        }
        if (ZipperUtil.isBlockFree(world, pos.east()) || isBlockUnpassable(world, pos, Direction.EAST)) {
            shape = VoxelShapes.join(shape, VoxelShapes.box(1, 0, 0, 0.99, 1, 1), IBooleanFunction.OR);
        }
        return shape;
    }

    @Unique
    private static boolean isBlockUnpassable(IBlockReader world, BlockPos blockPos, Direction direction) {
        return world.getBlockState(blockPos.relative(direction)).getBlock() instanceof DoorBlock
                || world.getBlockState(blockPos.relative(direction)).getBlock() instanceof TrapDoorBlock
                || world.getBlockState(blockPos.relative(direction)).getBlock() instanceof FlowingFluidBlock
                || (world.getBlockState(blockPos.relative(direction)).getBlock() instanceof StickyFingersZipperBlock
        && world.getBlockState(blockPos.relative(direction)).getValue(StickyFingersZipperBlock.INITIAL_FACING) != direction
        && world.getBlockState(blockPos.relative(direction)).getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite())));
    }

    @Unique
    private static boolean isBlockVanillaDoor(IBlockReader world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock() instanceof DoorBlock
                || world.getBlockState(blockPos).getBlock() instanceof TrapDoorBlock;
    }

    @Unique
    private static boolean checkFace(IBlockReader world, BlockPos blockPos, Direction direction) {
        return world.getBlockState(blockPos.relative(direction)).isFaceSturdy(world, blockPos.relative(direction), direction.getOpposite());
    }

    @Inject(at = @At("HEAD"), method = "isSuffocating", cancellable = true)
    private void cancelSuffocation(IBlockReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if (hasZippersAround(pos, world)){
            cir.setReturnValue(false);
        }
    }
}
