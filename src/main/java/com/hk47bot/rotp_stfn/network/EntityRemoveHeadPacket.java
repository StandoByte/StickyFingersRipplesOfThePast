package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EntityRemoveHeadPacket {
    private final int headEntityId;

    public EntityRemoveHeadPacket(int headEntityId){
        this.headEntityId = headEntityId;
    }
    public static class Handler implements IModPacketHandler<EntityRemoveHeadPacket> {
        @Override
        public void encode(EntityRemoveHeadPacket entityRemoveHeadPacket, PacketBuffer buf) {
            buf.writeInt(entityRemoveHeadPacket.headEntityId);
        }
        @Override
        public EntityRemoveHeadPacket decode(PacketBuffer buf) {
            return new EntityRemoveHeadPacket(buf.readInt());
        }

        @Override
        public void handle(EntityRemoveHeadPacket entityZipperCapSyncPacket, Supplier<NetworkEvent.Context> ctx) {
            Entity head = ClientUtil.getEntityById(entityZipperCapSyncPacket.headEntityId);
            if (head instanceof PlayerHeadEntity && ((PlayerHeadEntity) head).getOwner() == Minecraft.getInstance().player){
                ClientUtil.setCameraEntityPreventShaderSwitch(head);
            }

        }

        @Override
        public Class<EntityRemoveHeadPacket> getPacketClass() {
            return EntityRemoveHeadPacket.class;
        }
    }
}
