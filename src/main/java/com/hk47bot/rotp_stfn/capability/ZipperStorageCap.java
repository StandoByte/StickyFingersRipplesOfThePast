package com.hk47bot.rotp_stfn.capability;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.ZipperStorageSyncPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ZipperStorageCap {
    private final World world;
    private ArrayList<BlockZipperStorage> blockStorages = new ArrayList<>();
    private ArrayList<EntityZipperStorage> entityStorages = new ArrayList<>();

    public ZipperStorageCap(World world) {
        this.world = world;
    }

    public void tick(){
        updateStorages();
    }

    private void updateStorages(){
        if (!world.isClientSide()){
            ArrayList<EntityZipperStorage> entityStoragesToRemove = new ArrayList<>();
            ArrayList<BlockZipperStorage> blockStoragesToRemove = new ArrayList<>();
            blockStorages.forEach(blockZipperStorage -> {
                if (world.getBlockState(blockZipperStorage.getPos()).isAir()){
                    InventoryHelper.dropContents(world, blockZipperStorage.getPos(), blockZipperStorage);
                    blockStoragesToRemove.add(blockZipperStorage);
                }
            });
            entityStorages.forEach(entityZipperStorage -> {
                Entity storageEntity = ((ServerWorld) world).getEntity(entityZipperStorage.getMobUUID());
                if (storageEntity != null
                        && !(storageEntity instanceof PlayerEntity && IStandPower.getPlayerStandPower((PlayerEntity) storageEntity).getType() == InitStands.STAND_STICKY_FINGERS.getStandType())
                        && !storageEntity.isAlive()){
                    InventoryHelper.dropContents(world, ((ServerWorld) world).getEntity(entityZipperStorage.getMobUUID()), entityZipperStorage);
                    entityStoragesToRemove.add(entityZipperStorage);
                }
            });
            entityStoragesToRemove.forEach(storageToRemove -> entityStorages.remove(storageToRemove));
            blockStoragesToRemove.forEach(storageToRemove -> blockStorages.remove(storageToRemove));
        }
    }

    @Nullable
    public EntityZipperStorage findEntityStorage(UUID entityId, ServerPlayerEntity player){
        Optional<EntityZipperStorage> storage = entityStorages.stream().filter(entityZipperStorage -> entityZipperStorage.getMobUUID().toString().equals(entityId.toString())).findFirst();
        if (!storage.isPresent()){
            EntityZipperStorage newStorage = new EntityZipperStorage(((ServerWorld)player.level).getEntity(entityId).getDisplayName(), entityId);
            entityStorages.add(newStorage);
            return newStorage;
        }
        return storage.get();
    }

    @Nullable
    public BlockZipperStorage findBlockStorage(BlockPos pos, ServerPlayerEntity player){

        Optional<BlockZipperStorage> storage = blockStorages.stream().filter(blockZipperStorage ->
                   blockZipperStorage.getPos().getX() == pos.getX()
                && blockZipperStorage.getPos().getY() == pos.getY()
                && blockZipperStorage.getPos().getZ() == pos.getZ()).findFirst();

        if (!storage.isPresent()){
            BlockZipperStorage newStorage = new BlockZipperStorage(pos, world.getBlockState(pos).getBlock().getName());
            blockStorages.add(newStorage);
            return newStorage;
        }
        return storage.get();
    }

    public void setEntityStorages(ArrayList<EntityZipperStorage> storages){
        this.entityStorages = storages;
    }

    public void syncData(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            AddonPackets.sendToClient(new ZipperStorageSyncPacket(entityStorages), (ServerPlayerEntity) player);
        }
    }

    public CompoundNBT toNBT(){
        CompoundNBT returnableNBT = new CompoundNBT();
        ListNBT entityStoragesNBT = new ListNBT();
        ListNBT blockStoragesNBT = new ListNBT();
        for (EntityZipperStorage entityZipperStorage : entityStorages){
            ListNBT storageNbt = new ListNBT();
            ListNBT compounds = new ListNBT();
            CompoundNBT identifier = new CompoundNBT();
            identifier.putUUID("ID", entityZipperStorage.getMobUUID());
            identifier.putString("Name", ITextComponent.Serializer.toJson(entityZipperStorage.getDisplayName()));
            compounds.add(identifier);
            storageNbt.add(compounds);
            storageNbt.add(entityZipperStorage.createTag());
            entityStoragesNBT.add(storageNbt);
        }
        for (BlockZipperStorage blockZipperStorage : blockStorages){
            ListNBT storageNbt = new ListNBT();
            ListNBT compounds = new ListNBT();
            CompoundNBT identifier = new CompoundNBT();
            identifier.putLong("BlockPos", blockZipperStorage.getPos().asLong());
            identifier.putString("Name", ITextComponent.Serializer.toJson(blockZipperStorage.getDisplayName()));
            compounds.add(identifier);
            storageNbt.add(compounds);
            storageNbt.add(blockZipperStorage.createTag());
            blockStoragesNBT.add(storageNbt);
        }
        returnableNBT.put("EntityStorages", entityStoragesNBT);
        returnableNBT.put("BlockStorages", blockStoragesNBT);
        return returnableNBT;
    }

    public void fromNBT(CompoundNBT nbt){
        ListNBT entityStorageListNBT = (ListNBT) nbt.get("EntityStorages");
        for (INBT inbt : entityStorageListNBT) {
            ListNBT storageNbt = (ListNBT)inbt;
            for (int i = 0; i < storageNbt.size(); i+=2) {
                CompoundNBT idNBT = (CompoundNBT)((ListNBT)storageNbt.get(i)).get(0);
                EntityZipperStorage storage = new EntityZipperStorage(ITextComponent.Serializer.fromJson(idNBT.getString("Name")), idNBT.getUUID("ID"));
                storage.fromTag((ListNBT) storageNbt.get(i+1));
                entityStorages.add(storage);
            }
        }
        ListNBT blockStorageListNBT = (ListNBT) nbt.get("BlockStorages");
        for (INBT inbt : blockStorageListNBT) {
            ListNBT storageNbt = (ListNBT)inbt;
            for (int i = 0; i < storageNbt.size(); i+=2) {
                CompoundNBT idNBT = (CompoundNBT)((ListNBT)storageNbt.get(i)).get(0);
                BlockZipperStorage storage = new BlockZipperStorage(BlockPos.of(idNBT.getLong("BlockPos")), ITextComponent.Serializer.fromJson(idNBT.getString("Name")));
                storage.fromTag((ListNBT) storageNbt.get(i+1));
                blockStorages.add(storage);
            }
        }
    }

}
