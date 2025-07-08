package com.hk47bot.rotp_stfn.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ZipperStorageCapStorage implements Capability.IStorage<ZipperStorageCap> {

    @Override
    public INBT writeNBT(Capability<ZipperStorageCap> capability, ZipperStorageCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<ZipperStorageCap> capability, ZipperStorageCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
