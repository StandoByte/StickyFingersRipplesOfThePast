package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.container.BlockZipperContainer;
import com.hk47bot.rotp_stfn.container.EntityZipperContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<ContainerType<EntityZipperContainer>> STICKY_FINGERS_ENTITY_CONTAINER = CONTAINERS.register("stfn_entity_container",
            () -> IForgeContainerType.create((EntityZipperContainer::new)));

    public static final RegistryObject<ContainerType<BlockZipperContainer>> STICKY_FINGERS_BLOCK_CONTAINER = CONTAINERS.register("stfn_block_container",
            () -> IForgeContainerType.create((BlockZipperContainer::new)));
}
