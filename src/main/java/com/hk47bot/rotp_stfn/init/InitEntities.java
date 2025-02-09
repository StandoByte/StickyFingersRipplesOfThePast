package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.entity.projectile.ExtendedPunchEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<EntityType<ExtendedPunchEntity>> EXTENDED_PUNCH = ENTITIES.register("sf_extended_punch",
            () -> EntityType.Builder.<ExtendedPunchEntity>of(ExtendedPunchEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).noSummon().noSave().setUpdateInterval(20)
                    .build(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "stfn_extended_punch").toString()));
    
};
