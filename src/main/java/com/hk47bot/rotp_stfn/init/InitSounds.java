package com.hk47bot.rotp_stfn.init;

import java.util.function.Supplier;

import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpStickyFingersAddon.MOD_ID);


    
    public static final RegistryObject<SoundEvent> BRUNO_STICKY_FINGERS = SOUNDS.register("bruno_summon",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_summon")));

    public static final RegistryObject<SoundEvent> BRUNO_FINISHER = SOUNDS.register("bruno_finisher",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_finisher")));

    public static final RegistryObject<SoundEvent> BRUNO_BARRAGE = SOUNDS.register("bruno_barrage",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_barrage")));

    public static final RegistryObject<SoundEvent> BRUNO_HEAVY_PUNCH = SOUNDS.register("bruno_heavy_punch",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_heavy_punch")));

    public static final RegistryObject<SoundEvent> BRUNO_PUNCH = SOUNDS.register("bruno_punch",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_punch")));

    public static final RegistryObject<SoundEvent> BRUNO_ZIP = SOUNDS.register("bruno_zip",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_zip")));

    public static final RegistryObject<SoundEvent> BRUNO_UNZIP = SOUNDS.register("bruno_unzip",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "bruno_unzip")));

    public static final Supplier<SoundEvent> STICKY_FINGERS_SUMMON = SOUNDS.register("sticky_fingers_summon",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_summon")));
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_UNSUMMON = SOUNDS.register("sticky_fingers_unsummon",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_unsummon")));
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_PUNCH_LIGHT = SOUNDS.register("sticky_fingers_punch",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_punch")));
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_PUNCH_HEAVY = SOUNDS.register("sticky_fingers_heavy_punch",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_heavy_punch")));
    
    public static final Supplier<SoundEvent> STICKY_FINGERS_BARRAGE = STICKY_FINGERS_PUNCH_LIGHT;

    public static final Supplier<SoundEvent> STICKY_FINGERS_FINISHER = SOUNDS.register("sticky_fingers_finisher",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_finisher")));

    public static final Supplier<SoundEvent> STICKY_FINGERS_EXTENDED_PUNCH = SOUNDS.register("sticky_fingers_extended_punch_swing",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_extended_punch_swing")));

    public static final Supplier<SoundEvent> STICKY_FINGERS_UNZIP_PART = SOUNDS.register("sticky_fingers_unzip_part",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_unzip_part")));

    public static final Supplier<SoundEvent> ZIPPER_CREATE = SOUNDS.register("zipper_create",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "zipper_create")));

    public static final Supplier<SoundEvent> ZIPPER_OPEN = SOUNDS.register("zipper_open",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "zipper_open")));

    public static final Supplier<SoundEvent> ZIPPER_CLOSE = SOUNDS.register("zipper_close",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "zipper_close")));

    public static final Supplier<SoundEvent> ZIPPER_REMOVE = SOUNDS.register("zipper_remove",
            () -> new SoundEvent(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "zipper_remove")));

    public static final Supplier<SoundEvent> ZIPPER_STEP = SOUNDS.register("zipper_step",
            () -> new SoundEvent(new ResourceLocation("block.netherite_block.step")));

    public static final Supplier<SoundEvent> ZIPPER_HIT = SOUNDS.register("zipper_hit",
            () -> new SoundEvent(new ResourceLocation("block.netherite_block.hit")));

    public static final Supplier<SoundEvent> ZIPPER_FALL = SOUNDS.register("zipper_fall",
            () -> new SoundEvent(new ResourceLocation("block.netherite_block.fall")));

    public static final ForgeSoundType ZIPPER_BLOCK = new ForgeSoundType(1.0F, 1.0F, ZIPPER_REMOVE, ZIPPER_STEP, ZIPPER_CREATE, ZIPPER_HIT, ZIPPER_FALL);

    static final OstSoundList STICKY_FINGERS_OST = new OstSoundList(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "sticky_fingers_ost"), SOUNDS);


}
