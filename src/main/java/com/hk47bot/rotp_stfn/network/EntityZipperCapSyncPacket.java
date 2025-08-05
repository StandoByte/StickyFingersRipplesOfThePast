package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
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
    private final boolean hasHead;

    private final int headId;
    private final int leftArmId;
    private final int rightArmId;
    private final int rightLegId;
    private final int leftLegId;

    public EntityZipperCapSyncPacket(
            int entityId,
            boolean leftArmBlocked,
            boolean rightArmBlocked,
            boolean leftLegBlocked,
            boolean rightLegBlocked,
            boolean hasHead,

            int headId,
            int leftArmId,
            int rightArmId,
            int leftLegId,
            int rightLegId
    ) {
        this.entityId = entityId;
        this.leftArmBlocked = leftArmBlocked;
        this.rightArmBlocked = rightArmBlocked;
        this.leftLegBlocked = leftLegBlocked;
        this.rightLegBlocked = rightLegBlocked;
        this.hasHead = hasHead;

        this.headId = headId;
        this.leftArmId = leftArmId;
        this.rightArmId = rightArmId;
        this.rightLegId = rightLegId;
        this.leftLegId = leftLegId;
    }

    public EntityZipperCapSyncPacket(EntityZipperCapability capability) {
        this.entityId = capability.getEntityId();
        this.leftArmBlocked = capability.isLeftArmBlocked();
        this.rightArmBlocked = capability.isRightArmBlocked();
        this.leftLegBlocked = capability.isLeftLegBlocked();
        this.rightLegBlocked = capability.isRightLegBlocked();
        this.hasHead = capability.isHasHead();

        this.headId = capability.getHeadId();
        this.leftArmId = capability.getLeftArmId();
        this.rightArmId = capability.getRightArmId();
        this.rightLegId = capability.getRightLegId();
        this.leftLegId = capability.getLeftLegId();
    }

    public static class Handler implements IModPacketHandler<EntityZipperCapSyncPacket> {
        @Override
        public void encode(EntityZipperCapSyncPacket entityZipperCapSyncPacket, PacketBuffer buf) {
            buf.writeInt(entityZipperCapSyncPacket.entityId);
            buf.writeBoolean(entityZipperCapSyncPacket.leftArmBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.rightArmBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.leftLegBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.rightLegBlocked);
            buf.writeBoolean(entityZipperCapSyncPacket.hasHead);

            buf.writeInt(entityZipperCapSyncPacket.headId);
            buf.writeInt(entityZipperCapSyncPacket.leftArmId);
            buf.writeInt(entityZipperCapSyncPacket.rightArmId);
            buf.writeInt(entityZipperCapSyncPacket.rightLegId);
            buf.writeInt(entityZipperCapSyncPacket.leftLegId);
        }

        @Override
        public EntityZipperCapSyncPacket decode(PacketBuffer buf) {
            return new EntityZipperCapSyncPacket(
                    buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
                    buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
        }

        @Override
        public void handle(EntityZipperCapSyncPacket entityZipperCapSyncPacket, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(entityZipperCapSyncPacket.entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).ifPresent(capability -> {
                    capability.setLeftArmBlocked(entityZipperCapSyncPacket.leftArmBlocked);
                    capability.setRightArmBlocked(entityZipperCapSyncPacket.rightArmBlocked);

                    capability.setLeftLegBlocked(entityZipperCapSyncPacket.leftLegBlocked);
                    capability.setRightLegBlocked(entityZipperCapSyncPacket.rightLegBlocked);

                    capability.setHead(entityZipperCapSyncPacket.hasHead);

                    capability.setHeadId(entityZipperCapSyncPacket.headId);
                    capability.setLeftArmId(entityZipperCapSyncPacket.leftArmId);
                    capability.setRightArmId(entityZipperCapSyncPacket.rightArmId);
                    capability.setRightLegId(entityZipperCapSyncPacket.rightLegId);
                    capability.setLeftLegId(entityZipperCapSyncPacket.leftLegId);
                });
            }
        }

        @Override
        public Class<EntityZipperCapSyncPacket> getPacketClass() {
            return EntityZipperCapSyncPacket.class;
        }
    }
}
