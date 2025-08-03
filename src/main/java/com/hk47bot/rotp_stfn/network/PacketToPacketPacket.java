package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToPacketPacket {
    private final int headEntityId;

    public PacketToPacketPacket(int headEntityId){
        this.headEntityId = headEntityId;
    }
    public static class Handler implements IModPacketHandler<PacketToPacketPacket> {
        @Override
        public void encode(PacketToPacketPacket entityRemoveHeadPacket, PacketBuffer buf) {
            buf.writeInt(entityRemoveHeadPacket.headEntityId);
        }
        @Override
        public PacketToPacketPacket decode(PacketBuffer buf) {
            return new PacketToPacketPacket(buf.readInt());
        }

        @Override
        public void handle(PacketToPacketPacket entityZipperCapSyncPacket, Supplier<NetworkEvent.Context> ctx) {
            Entity head = ctx.get().getSender().level.getEntity(entityZipperCapSyncPacket.headEntityId);
            if (head instanceof PlayerHeadEntity && ((PlayerHeadEntity) head).getOwner() != null){
                AddonPackets.sendToClient(new EntityRemoveHeadPacket(head.getId()), (ServerPlayerEntity) ((PlayerHeadEntity) head).getOwner());
            }
        }

        @Override
        public Class<PacketToPacketPacket> getPacketClass() {
            return PacketToPacketPacket.class;
        }
    }
}
