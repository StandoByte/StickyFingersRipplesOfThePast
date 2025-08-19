package com.hk47bot.rotp_stfn.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ZipperWorldCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(ZipperWorldCap.class)
    public static Capability<ZipperWorldCap> CAPABILITY = null;
    private LazyOptional<ZipperWorldCap> instance;

    public ZipperWorldCapProvider(World world) {
        this.instance = LazyOptional.of(() -> new ZipperWorldCap(world));
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
