package com.hk47bot.rotp_stfn.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ZipperWorldCapStorage implements Capability.IStorage<ZipperWorldCap> {

    @Override
    public INBT writeNBT(Capability<ZipperWorldCap> capability, ZipperWorldCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<ZipperWorldCap> capability, ZipperWorldCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
