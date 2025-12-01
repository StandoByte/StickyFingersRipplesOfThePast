package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.hk47bot.rotp_stfn.init.InitSounds.ZIPPER_BLOCK;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<StickyFingersZipperBlock> STICKY_FINGERS_ZIPPER = BLOCKS.register("sticky_fingers_zipper",
            () -> new StickyFingersZipperBlock(StickyFingersZipperBlock.Properties.of(Material.BARRIER).sound(ZIPPER_BLOCK).strength(3500000).noOcclusion()));

}
