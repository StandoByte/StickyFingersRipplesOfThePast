package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.capability.entity.living.LivingWallClimbing;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClStopWallClimbPacket;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.CollisionUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.ClStopZipperClimbPacket;
import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Optional;

import static com.github.standobyte.jojo.action.non_stand.HamonWallClimbing2.NO_CLIMBING_ON_BARRIERS;


public class StickyFingersClimbZipper extends StandAction {
    public StickyFingersClimbZipper(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<LivingWallClimbing> playerData = LivingWallClimbing.getHandler(user);
        boolean isWallClimbing = playerData.map(LivingWallClimbing::isWallClimbing).orElse(false);
        if (!isWallClimbing &&  target.getType() == ActionTarget.TargetType.BLOCK && target.getFace() != null && target.getFace().getAxis() != Direction.Axis.Y) {
            Direction blockFace = target.getFace();
            Vector3d vecToBlock = Vector3d.atLowerCornerOf(blockFace.getOpposite().getNormal()).scale(0.5);
            Vector3d collide = collide(user, user.getBoundingBox(), vecToBlock, true);
            if (!collide.equals(vecToBlock) && user.level.getBlockState(target.getBlockPos()).getBlock() instanceof StickyFingersZipperBlock) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
    }
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            Direction face = target.getFace();
            if (ZipperUtil.isBlockZipper(world, target.getBlockPos())){
                Vector3d vecToBlock = Vector3d.atLowerCornerOf(face.getOpposite().getNormal()).scale(0.5);
                Vector3d collide = collide(user, user.getBoundingBox(), vecToBlock, true);
                double distanceFromWall = user.getBbWidth() * 0.15;
                Vector3d moveTo = user.position().add(collide).add(Vector3d.atLowerCornerOf(face.getNormal()).scale(distanceFromWall));
                user.teleportTo(moveTo.x, moveTo.y, moveTo.z);
                if (user instanceof PlayerEntity) {
                    ((PlayerEntity) user).displayClientMessage(new TranslationTextComponent(
                            "jojo.message.wall_climb.hint_jump", new KeybindTextComponent("key.jump")), true);
                }
                user.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(zipperCap -> {
                    zipperCap.setWallClimbing(true);
                    zipperCap.setClimbStartPos(target.getBlockPos());
                });
            }
        }
    }

    public static boolean travelOnZipper(PlayerEntity player, Vector3d inputVec){
        EntityZipperCapability zipperCap = player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve().get();
        boolean isZipperClimbing = player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).map(EntityZipperCapability::isWallClimbing).orElse(false);
        if (isZipperClimbing){
            Optional<LivingWallClimbing> playerData = LivingWallClimbing.getHandler(player);
            boolean isWallClimbing = playerData.map(LivingWallClimbing::isWallClimbing).orElse(false);
            if (isWallClimbing) stopWallClimbing(player, playerData.get());
            player.fallDistance = 0;
            if (player.isLocalPlayer()) {
                AxisAlignedBB axisalignedbb = player.getBoundingBox();
                BlockPos blockpos = new BlockPos(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
                BlockPos blockpos1 = new BlockPos(axisalignedbb.maxX - 0.001D, (axisalignedbb.maxY - 0.001D) - (0.5 * axisalignedbb.getYsize()), axisalignedbb.maxZ - 0.001D);
                BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
                if (player.level.hasChunksAt(blockpos, blockpos1)) {
                    for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                        for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                            for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                                blockpos$mutable.set(i, j, k);
                                if (!ZipperUtil.isBlockZipper(player.level, blockpos$mutable)){
                                    stopZipperClimbing(player, zipperCap);
                                    return false;
                                }
                            }
                        }
                    }
                }
                Vector3d movement = new Vector3d(0, inputVec.z * 0.5d, 0);
                if (movement.lengthSqr() > 1) {
                    movement = movement.normalize();
                }
                ClientPlayerEntity clientPlayer = (ClientPlayerEntity) player;
                boolean isJumping = clientPlayer.input.jumping;
                boolean standOnGround = player.isOnGround() && player.zza < 0;
                if (standOnGround || isJumping) {
                    stopZipperClimbing(player, zipperCap);
                    return false;
                }
                player.setDeltaMovement(movement);
                player.move(MoverType.SELF, player.getDeltaMovement());
                player.calculateEntityAnimation(player, false);
                return true;
            }
        }
        return false;
    }

    private static void stopZipperClimbing(PlayerEntity player, EntityZipperCapability zipperCap) {
        if (!player.level.isClientSide()) {
            zipperCap.setWallClimbing(false);
        }
        else if (player.isLocalPlayer()) {
            AddonPackets.sendToServer(new ClStopZipperClimbPacket());
        }
    }

    private static void stopWallClimbing(PlayerEntity player, LivingWallClimbing wallClimbing) {
        if (!player.level.isClientSide()) {
            wallClimbing.stopWallClimbing();
        }
        else if (player.isLocalPlayer()) {
            PacketManager.sendToServer(new ClStopWallClimbPacket());
        }
    }

    private static Vector3d collide(Entity entity, AxisAlignedBB collisionBox, Vector3d offsetVec, boolean excludeBarriers) {
        return CollisionUtil.collide(entity, collisionBox, offsetVec, excludeBarriers ? NO_CLIMBING_ON_BARRIERS : null);
    }

}
