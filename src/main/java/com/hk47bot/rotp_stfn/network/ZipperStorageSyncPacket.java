package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.hk47bot.rotp_stfn.capability.EntityZipperStorage;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ZipperStorageSyncPacket {
    private final ArrayList<EntityZipperStorage> storages;

    public ZipperStorageSyncPacket(ArrayList<EntityZipperStorage> storages){
        this.storages = storages;
    }

    public static class Handler implements IModPacketHandler<ZipperStorageSyncPacket> {
        private final Minecraft mc = Minecraft.getInstance();
        @Override
        public void encode(ZipperStorageSyncPacket msg, PacketBuffer buffer) {
            buffer.writeInt(msg.storages.size());
            msg.storages.forEach(entityZipperStorage -> {
                buffer.writeInt(entityZipperStorage.getContainerSize());
                for (int i = 0; i <= entityZipperStorage.getContainerSize(); i++){
                    buffer.writeItem(entityZipperStorage.getItem(i));

                }
            });
        }
        @Override
        public ZipperStorageSyncPacket decode(PacketBuffer buf) {
            ArrayList<EntityZipperStorage> storages = new ArrayList<>();
            int storagesCount = buf.readInt();
            for (int i = 0; i < storagesCount; i++) {
//                mc.level.getEntities(null, )
                LivingEntity entity = (LivingEntity) mc.level.getEntity(buf.readInt());
                EntityZipperStorage storage = new EntityZipperStorage(entity.getName(), entity.getUUID());
                for (int j = 0; j < buf.readInt(); j++) {
                    storage.addItem(buf.readItem());
                }
                storages.add(storage);
            }
            return new ZipperStorageSyncPacket(storages);
        }
        @Override
        public void handle(ZipperStorageSyncPacket zipperStorageSyncPacket, Supplier<NetworkEvent.Context> ctx) {
            mc.level.getCapability(ZipperStorageCapProvider.CAPABILITY).ifPresent(zipperStorageCap -> zipperStorageCap.setEntityStorages(zipperStorageSyncPacket.storages));
        }
        @Override
        public Class<ZipperStorageSyncPacket> getPacketClass() {
            return ZipperStorageSyncPacket.class;
        }
    }
}
