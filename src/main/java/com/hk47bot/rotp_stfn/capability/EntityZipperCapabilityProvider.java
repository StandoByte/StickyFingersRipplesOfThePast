package com.hk47bot.rotp_stfn.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class EntityZipperCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(EntityZipperCapability.class)
    public static Capability<EntityZipperCapability> CAPABILITY = null;
    private LazyOptional<EntityZipperCapability> instance;

    public EntityZipperCapabilityProvider(LivingEntity entity) {
        this.instance = LazyOptional.of(() -> new EntityZipperCapability(entity));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("LivingEntity capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("LivingEntity capability LazyOptional is not attached.")), null, nbt);
    }
}
