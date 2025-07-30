package com.hk47bot.rotp_stfn.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class EntityZipperCapabilityStorage implements Capability.IStorage<EntityZipperCapability> {

    @Override
    public INBT writeNBT(Capability<EntityZipperCapability> capability, EntityZipperCapability instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<EntityZipperCapability> capability, EntityZipperCapability instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
