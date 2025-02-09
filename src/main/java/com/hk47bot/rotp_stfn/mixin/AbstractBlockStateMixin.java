package com.hk47bot.rotp_stfn.mixin;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.EntityZipperDataProvider;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.EntitySelectionContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow protected abstract BlockState asState();

    @Unique
    private boolean isAbove(Entity entity, VoxelShape shape, BlockPos pos, boolean defaultValue) {
        return entity.getY() > (double)pos.getY() + shape.max(Direction.Axis.Y) - (entity.isOnGround() ? 8.05/16.0 : 0.0015);
    }

    @Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;", cancellable = true)
    private void phaseThroughBlocks(IBlockReader world, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> info) {
        VoxelShape blockShape = getBlock().getCollisionShape(this.asState(), world, pos, context);
        if(!blockShape.isEmpty() && context instanceof EntitySelectionContext) {
            EntitySelectionContext esc = (EntitySelectionContext)context;
            if(esc.getEntity() != null) {
                Entity entity = esc.getEntity();
                if (entity instanceof LivingEntity){
                    entity.getCapability(EntityZipperDataProvider.CAPABILITY).ifPresent(cap -> {
                        if (cap.getRemainingZipperTicks() > 0 && cap.getRemainingZipperCooldown() == 0){
                            if (!this.isAbove(entity, blockShape, pos, false) || (cap.getEnterDirection() == Direction.DOWN && entity.isShiftKeyDown() && world.getBlockState(pos).getBlock() != Blocks.BEDROCK)){
                                ZipperUtil.stopMovementInZipper(cap.getEnterDirection(), entity);
                                info.setReturnValue(VoxelShapes.empty());
                            }
                        }
                    });
                }
            }
        }
    }
}
