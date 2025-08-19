package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

public class HumanoidListPacket {
    private final ArrayList<EntityType> humanoidTypes;
    public HumanoidListPacket(ArrayList<EntityType> humanoidTypes) {
        this.humanoidTypes = humanoidTypes;
    }

    public HumanoidListPacket(ZipperWorldCap cap) {
        humanoidTypes = cap.humanoidTypes;
    }

    public static class Handler implements IModPacketHandler<HumanoidListPacket> {

        @Override
        public void encode(HumanoidListPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.humanoidTypes.size());
            for (EntityType type : msg.humanoidTypes){
                buf.writeResourceLocation(EntityType.getKey(type));
            }
        }

        @Override
        public HumanoidListPacket decode(PacketBuffer buf) {
            ArrayList<EntityType> humanoidTypes = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                ResourceLocation location = buf.readResourceLocation();
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(location);
                humanoidTypes.add(type);
            }
            return new HumanoidListPacket(humanoidTypes);
        }

        @Override
        public void handle(HumanoidListPacket msg, Supplier<NetworkEvent.Context> ctx) {
            World world = ctx.get().getSender().getLevel();
            world.getCapability(ZipperWorldCapProvider.CAPABILITY).ifPresent(cap -> {
                cap.humanoidTypes = msg.humanoidTypes;
            });
        }

        @Override
        public Class<HumanoidListPacket> getPacketClass() {
            return HumanoidListPacket.class;
        }
    }
}
