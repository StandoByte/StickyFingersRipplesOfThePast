package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class EntityZipperCapSyncPacket {
    private final int entityId;
    private final boolean leftArmBlocked;
    private final boolean rightArmBlocked;
    private final boolean rightLegBlocked;
    private final boolean leftLegBlocked;
    private final boolean hasHead;

    private final UUID headId;
    private final UUID leftArmId;
    private final UUID rightArmId;
    private final UUID rightLegId;
    private final UUID leftLegId;

    public EntityZipperCapSyncPacket(
            int entityId,
            boolean leftArmBlocked,
            boolean rightArmBlocked,
            boolean leftLegBlocked,
            boolean rightLegBlocked,
            boolean hasHead,

            UUID headId,
            UUID leftArmId,
            UUID rightArmId,
            UUID leftLegId,
            UUID rightLegId
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
        public void encode(EntityZipperCapSyncPacket packet, PacketBuffer buf) {
            buf.writeInt(packet.entityId);
            buf.writeBoolean(packet.leftArmBlocked);
            buf.writeBoolean(packet.rightArmBlocked);
            buf.writeBoolean(packet.leftLegBlocked);
            buf.writeBoolean(packet.rightLegBlocked);
            buf.writeBoolean(packet.hasHead);

            buf.writeBoolean(packet.headId != null);
            if (packet.headId != null) {
                buf.writeUUID(packet.headId);
            }

            buf.writeBoolean(packet.leftArmId != null);
            if (packet.leftArmId != null) {
                buf.writeUUID(packet.leftArmId);
            }

            buf.writeBoolean(packet.rightArmId != null);
            if (packet.rightArmId != null) {
                buf.writeUUID(packet.rightArmId);
            }

            buf.writeBoolean(packet.rightLegId != null);
            if (packet.rightLegId != null) {
                buf.writeUUID(packet.rightLegId);
            }

            buf.writeBoolean(packet.leftLegId != null);
            if (packet.leftLegId != null) {
                buf.writeUUID(packet.leftLegId);
            }
        }

        @Override
        public EntityZipperCapSyncPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            boolean leftArmBlocked = buf.readBoolean();
            boolean rightArmBlocked = buf.readBoolean();
            boolean leftLegBlocked = buf.readBoolean();
            boolean rightLegBlocked = buf.readBoolean();
            boolean hasHead = buf.readBoolean();

            UUID headId = buf.readBoolean() ? buf.readUUID() : null;
            UUID leftArmId = buf.readBoolean() ? buf.readUUID() : null;
            UUID rightArmId = buf.readBoolean() ? buf.readUUID() : null;
            UUID rightLegId = buf.readBoolean() ? buf.readUUID() : null;
            UUID leftLegId = buf.readBoolean() ? buf.readUUID() : null;

            return new EntityZipperCapSyncPacket(entityId, leftArmBlocked, rightArmBlocked, leftLegBlocked, rightLegBlocked, hasHead,
                    headId, leftArmId, rightArmId, leftLegId, rightLegId);
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
