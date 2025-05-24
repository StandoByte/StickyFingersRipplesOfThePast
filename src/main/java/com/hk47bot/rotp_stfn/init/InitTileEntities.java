package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RotpStickyFingersAddon.MOD_ID);
    public static final RegistryObject<TileEntityType<StickyFingersZipperTileEntity>> ZIPPER_TILE_ENTITY = TILE_ENTITIES.register("zipper_tile_entity",
            () -> TileEntityType.Builder.of(StickyFingersZipperTileEntity::new, InitBlocks.STICKY_FINGERS_ZIPPER.get()).build(null));
}
