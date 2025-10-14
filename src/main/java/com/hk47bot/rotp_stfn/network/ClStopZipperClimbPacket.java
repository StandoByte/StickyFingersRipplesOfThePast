package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.action.stand.StickyFingersToggleZipper;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClStopZipperClimbPacket {
    
    public static class Handler implements IModPacketHandler<ClStopZipperClimbPacket> {

        @Override
        public void encode(ClStopZipperClimbPacket msg, PacketBuffer buf) {}

        @Override
        public ClStopZipperClimbPacket decode(PacketBuffer buf) {
            return new ClStopZipperClimbPacket();
        }

        @Override
        public void handle(ClStopZipperClimbPacket msg, Supplier<NetworkEvent.Context> ctx) {
            PlayerEntity player = ctx.get().getSender();
            if (player.isAlive()) {
                player.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(zipperCap -> {
                    zipperCap.setWallClimbing(false);
                    World world = player.level;
                    BlockPos targetedBlockPos = zipperCap.getClimbStartPos();
                    BlockState targetedState = world.getBlockState(targetedBlockPos);
                    StickyFingersToggleZipper.toggleZipper(world, targetedBlockPos, targetedState);
                });
            }
        }

        @Override
        public Class<ClStopZipperClimbPacket> getPacketClass() {
            return ClStopZipperClimbPacket.class;
        }
    }
    
}
