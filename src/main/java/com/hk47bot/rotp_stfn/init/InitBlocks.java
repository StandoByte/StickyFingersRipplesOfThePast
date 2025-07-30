package com.hk47bot.rotp_stfn.init;

import com.github.standobyte.jojo.block.StoneMaskBlock;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<StickyFingersZipperBlock2> STICKY_FINGERS_ZIPPER = BLOCKS.register("sticky_fingers_zipper_new",
            () -> new StickyFingersZipperBlock2(StickyFingersZipperBlock2.Properties.copy(Blocks.GOLD_BLOCK).noOcclusion()));

}
