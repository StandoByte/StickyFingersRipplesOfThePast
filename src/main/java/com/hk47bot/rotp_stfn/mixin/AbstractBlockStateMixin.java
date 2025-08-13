package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import net.minecraft.block.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.OPEN;
import static com.hk47bot.rotp_stfn.util.ZipperUtil.hasZippersAround;

@Mixin(value = AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow protected abstract BlockState asState();

    @Inject(method = "updateNeighbourShapes(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;II)V", at = @At("HEAD"), cancellable = true)
    private void iHateFuckingFinal(IWorld world, BlockPos pos, int p_241482_3_, int p_241482_4_, CallbackInfo ci){
        if (this.getBlock() instanceof StickyFingersZipperBlock2){
            BlockState state = world.getBlockState(pos);
            for (int i = -1; i < 2; i+=1) {
                for (int j = -1; j < 2; j+=1) {
                    for (int k = -1; k < 2; k+=1) {
                        BlockPos neighbourPos = pos.offset(i, j, k);
                        if (world.getBlockState(neighbourPos).getBlock() instanceof StickyFingersZipperBlock2
                                && world.getBlockState(neighbourPos).getValue(StickyFingersZipperBlock2.INITIAL_FACING) == state.getValue(StickyFingersZipperBlock2.INITIAL_FACING)){
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
                if (hasZippersAround(pos, world)){
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
            }
        }
        catch (Exception e){
            RotpStickyFingersAddon.getLogger().info("Checking zippers for unloaded chunks:");
            info.setReturnValue(blockShape);
        }
    }

    @Inject(at = @At("HEAD"), method = "isSuffocating", cancellable = true)
    private void cancelSuffocation(IBlockReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if (hasZippersAround(pos, world)){
            cir.setReturnValue(false);
        }
    }
}
