package com.hk47bot.rotp_stfn;

import com.hk47bot.rotp_stfn.capability.CapabilityHandler;
import com.hk47bot.rotp_stfn.init.*;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RotpStickyFingersAddon.MOD_ID)
public class RotpStickyFingersAddon {
    // The value here should match an entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_stfn";
    private static final Logger LOGGER = LogManager.getLogger();

    public RotpStickyFingersAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitContainers.CONTAINERS.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
        InitBlocks.BLOCKS.register(modEventBus);
        InitEntities.ENTITIES.register(modEventBus);
        InitStands.STAND_TYPES.register(modEventBus);
        InitTileEntities.TILE_ENTITIES.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::preInit);
    }
    private void preInit(FMLCommonSetupEvent event) {
        CapabilityHandler.commonSetupRegister();
        AddonPackets.init();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
