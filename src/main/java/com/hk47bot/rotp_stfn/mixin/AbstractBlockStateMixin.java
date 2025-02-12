package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow protected abstract BlockState asState();

//    @Unique
//    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos, boolean defaultValue) {
//        return entity.getY() > (double)pos.getY() + shape.max(Direction.Axis.Y) - (entity.isOnGround() ? 8.05/16.0 : 0.0015);
//    }

    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;", cancellable = true)
    private void phaseThroughBlocks(IBlockReader world, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> info) {
        VoxelShape blockShape = getBlock().getCollisionShape(this.asState(), world, pos, context);
        if(!blockShape.isEmpty() && context instanceof EntitySelectionContext) {
//            EntitySelectionContext esc = (EntitySelectionContext)context;
            if (this.stickyFingersRipplesOfThePast$hasZippersAround(pos, world)){
                VoxelShape shape = VoxelShapes.empty();

                BlockState upState = world.getBlockState(pos.above());
                BlockState downState = world.getBlockState(pos.below());
                BlockState northState = world.getBlockState(pos.north());
                BlockState southState = world.getBlockState(pos.south());
                BlockState westState = world.getBlockState(pos.west());
                BlockState eastState = world.getBlockState(pos.east());


                if (upState.getBlock() instanceof AirBlock) {
                    shape = VoxelShapes.join(shape, VoxelShapes.box(0.0, 1, 0.0, 1, 0.99, 1), IBooleanFunction.OR);
                }
                if (downState.getBlock() instanceof AirBlock) {
                    shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 0.01, 1), IBooleanFunction.OR);
                }
                if (northState.getBlock() instanceof AirBlock) {
                    shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 1, 0.01), IBooleanFunction.OR);
                }
                if (southState.getBlock() instanceof AirBlock) {
                    shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 1, 1, 1, 0.99), IBooleanFunction.OR);
                }
                if (westState.getBlock() instanceof AirBlock) {
                    shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 0.01, 1, 1), IBooleanFunction.OR);
                }
                if (eastState.getBlock() instanceof AirBlock) {
                    shape = VoxelShapes.join(shape, VoxelShapes.box(1, 0, 0, 0.99, 1, 1), IBooleanFunction.OR);
                }

                info.setReturnValue(shape);
            }

//            if(esc.getEntity() != null) {
//                Entity entity = esc.getEntity();
//                if (entity instanceof LivingEntity){
//                    entity.getCapability(EntityZipperDataProvider.CAPABILITY).ifPresent(cap -> {
//                        if (cap.getRemainingZipperTicks() > 0 && cap.getRemainingZipperCooldown() == 0){
//                            if (!this.isAbove(entity, blockShape, pos, false) || (cap.getEnterDirection() == Direction.DOWN && entity.isShiftKeyDown() && world.getBlockState(pos).getBlock() != Blocks.BEDROCK)){
//                                ZipperUtil.stopMovementInZipper(cap.getEnterDirection(), entity);
//                                info.setReturnValue(VoxelShapes.empty());
//                            }
//                        }
//                    });
//                }
//            }
        }
    }
    @Inject(at = @At("HEAD"), method = "isSuffocating", cancellable = true)
    private void cancelSuffocation(IBlockReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if (stickyFingersRipplesOfThePast$hasZippersAround(pos, world)){
            cir.setReturnValue(false);
        }
        else {
            cir.cancel();
        }
    }

//    @Inject(at = @At("HEAD"), method = "isViewBlocking", cancellable = true)
//    private void cancelViewBlocking(IBlockReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
//        if (stickyFingersRipplesOfThePast$hasZippersAround(pos, world)){
//            cir.setReturnValue(false);
//        }
//        else {
//            cir.cancel();
//        }
//    }


    @Unique
    private boolean stickyFingersRipplesOfThePast$hasZippersAround(BlockPos pos, IBlockReader world){
        for (Direction direction : Direction.values()){
            if (world.getBlockState(ZipperUtil.getBlockInDirection(direction, pos)).getBlock() instanceof StickyFingersZipperBlock
                    && world.getBlockState(ZipperUtil.getBlockInDirection(direction, pos)).getValue(StickyFingersZipperBlock.DIRECTION) == direction){
                return true;
            }
        }
        return false;
    }
//    @Unique
//    private List<Direction> getZipperDirectionsAround(BlockPos pos, IBlockReader world){
//        List<Direction> directions = new ArrayList<>();
//        for (Direction direction : Direction.values()){
//            if (world.getBlockState(ZipperUtil.getBlockInDirection(direction, pos)).getBlock() instanceof StickyFingersZipperBlock){
//                directions.add(direction);
//            }
//        }
//        return directions;
//    }
}
