package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.item.ZipperDebugItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.standobyte.jojo.init.ModItems.MAIN_TAB;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<ZipperDebugItem> ZIPPER_DEBUGGER = ITEMS.register("zipper_debugger",
            () -> new ZipperDebugItem(new Item.Properties().tab(MAIN_TAB)));
}
