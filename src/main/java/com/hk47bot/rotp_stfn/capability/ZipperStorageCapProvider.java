package com.hk47bot.rotp_stfn.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ZipperStorageCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(ZipperStorageCap.class)
    public static Capability<ZipperStorageCap> CAPABILITY = null;
    private LazyOptional<ZipperStorageCap> instance;

    public ZipperStorageCapProvider(World world) {
        this.instance = LazyOptional.of(() -> new ZipperStorageCap(world));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached.")), null, nbt);
    }
}
