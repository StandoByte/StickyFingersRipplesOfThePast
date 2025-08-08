package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.effect.ZipWoundsEffect;
import com.hk47bot.rotp_stfn.effect.ZipperEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<Effect> ZIP_WOUNDS = EFFECTS.register ("zip_wounds",
            () -> new ZipWoundsEffect(EffectType.BENEFICIAL, 0xFFD264));

    public static final RegistryObject<Effect> ZIPPER = EFFECTS.register ("zipper",
            () -> new ZipperEffect(EffectType.HARMFUL, 0xCC9A30));
}
