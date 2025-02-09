package com.hk47bot.rotp_stfn;

import com.hk47bot.rotp_stfn.capability.CapabilityHandler;
import com.hk47bot.rotp_stfn.init.InitBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hk47bot.rotp_stfn.init.InitEntities;
import com.hk47bot.rotp_stfn.init.InitSounds;
import com.hk47bot.rotp_stfn.init.InitStands;

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
        InitBlocks.BLOCKS.register(modEventBus);
        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        modEventBus.addListener(this::preInit);
    }
    private void preInit(FMLCommonSetupEvent event) {
        CapabilityHandler.commonSetupRegister();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
