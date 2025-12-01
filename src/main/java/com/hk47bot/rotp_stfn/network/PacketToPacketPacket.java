package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.client.HumanoidParser;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToPacketPacket {


    public PacketToPacketPacket(){
    }
    public static class Handler implements IModPacketHandler<PacketToPacketPacket> {
        @Override
        public void encode(PacketToPacketPacket entityRemoveHeadPacket, PacketBuffer buf) {
        }
        @Override
        public PacketToPacketPacket decode(PacketBuffer buf) {
            return new PacketToPacketPacket();
        }

        @Override
        public void handle(PacketToPacketPacket entityZipperCapSyncPacket, Supplier<NetworkEvent.Context> ctx) {
            World world = ClientUtil.getClientWorld();
            ZipperWorldCap cap = world.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
            HumanoidParser.updateHumanoidsList(cap);
            if (cap != null) AddonPackets.sendToServer(new HumanoidListPacket(cap));

        }

        @Override
        public Class<PacketToPacketPacket> getPacketClass() {
            return PacketToPacketPacket.class;
        }
    }
}
