package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EntityZipperCapSyncPacket {
    private final int entityId;
    private final boolean leftArmBlocked;
    private final boolean rightArmBlocked;
    private final boolean rightLegBlocked;
    private final boolean leftLegBlocked;
    public EntityZipperCapSyncPacket(int entityId, boolean leftArmBlocked, boolean rightArmBlocked, boolean leftLegBlocked, boolean rightLegBlocked){
        this.entityId = entityId;
        this.leftArmBlocked = leftArmBlocked;
        this.rightArmBlocked = rightArmBlocked;
        this.leftLegBlocked = leftLegBlocked;
        this.rightLegBlocked = rightLegBlocked;
    }
    public EntityZipperCapSyncPacket(EntityZipperCapability capability){
        this.entityId = capability.getEntityId();
        this.leftLegBlocked = capability.isLeftLegBlocked();
        this.rightLegBlocked = capability.isRightLegBlocked();
        this.leftArmBlocked = capability.isLeftArmBlocked();
        this.rightArmBlocked = capability.isRightArmBlocked();
    }
    public static class Handler implements IModPacketHandler<EntityZipperCapSyncPacket> {
        private final Minecraft mc = Minecraft.getInstance();
        @Override
        public void encode(EntityZipperCapSyncPacket entityZipperCapSyncPacket, PacketBuffer buf) {
            buf.writeInt(entityZipperCapSyncPacket.entityId);
            buf.writeBoolean(entityZipperCapSyncPacket.leftArmBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.rightArmBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.leftLegBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.rightLegBlocked);
        }

        @Override
        public EntityZipperCapSyncPacket decode(PacketBuffer buf) {
            return new EntityZipperCapSyncPacket(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
        }

        @Override
        public void handle(EntityZipperCapSyncPacket entityZipperCapSyncPacket, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = mc.level.getEntity(entityZipperCapSyncPacket.entityId);
            if (entity instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(capability -> {
                    capability.setLeftArmBlocked(entityZipperCapSyncPacket.leftArmBlocked);
                    capability.setRightLegBlocked(entityZipperCapSyncPacket.rightArmBlocked);
                    capability.setLeftLegBlocked(entityZipperCapSyncPacket.leftLegBlocked);
                    capability.setRightLegBlocked(entityZipperCapSyncPacket.rightLegBlocked);
                });
            }
        }

        @Override
        public Class<EntityZipperCapSyncPacket> getPacketClass() {
            return EntityZipperCapSyncPacket.class;
        }
    }
}
