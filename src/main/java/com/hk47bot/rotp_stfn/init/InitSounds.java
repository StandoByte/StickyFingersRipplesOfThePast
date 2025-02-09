package com.hk47bot.rotp_stfn.init;

import java.util.function.Supplier;

import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpStickyFingersAddon.MOD_ID);
    
    public static final RegistryObject<SoundEvent> STICKY_FINGERS = SOUNDS.register("sticky_fingers", 
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers")));

    public static final Supplier<SoundEvent> STICKY_FINGERS_SUMMON = ModSounds.STAND_SUMMON_DEFAULT;
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_UNSUMMON = ModSounds.STAND_UNSUMMON_DEFAULT;
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_PUNCH_LIGHT = ModSounds.STAND_PUNCH_LIGHT;
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_PUNCH_HEAVY = ModSounds.STAND_PUNCH_HEAVY;
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_BARRAGE = ModSounds.STAND_PUNCH_LIGHT;

    static final OstSoundList STICKY_FINGERS_OST = new OstSoundList(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_ost"), SOUNDS);


}
