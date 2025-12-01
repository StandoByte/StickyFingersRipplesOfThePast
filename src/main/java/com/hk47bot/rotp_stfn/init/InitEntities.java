package com.hk47bot.rotp_stfn.init;

import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedArmEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedHeadEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedLegEntity;
import com.hk47bot.rotp_stfn.entity.projectile.ExtendedPunchEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, RotpStickyFingersAddon.MOD_ID);

    public static final RegistryObject<EntityType<ExtendedPunchEntity>> EXTENDED_PUNCH = ENTITIES.register("stfn_extended_punch",
            () -> EntityType.Builder.<ExtendedPunchEntity>of(ExtendedPunchEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).noSummon().noSave().setUpdateInterval(20)
                    .build(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "stfn_extended_punch").toString()));

    public static final RegistryObject<EntityType<UnzippedHeadEntity>> PLAYER_HEAD = ENTITIES.register("player_head",
            () -> EntityType.Builder.<UnzippedHeadEntity>of(UnzippedHeadEntity::new, EntityClassification.MISC).sized(0.6F, 0.6F).noSummon()
                    .build(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "player_head").toString()));

    public static final RegistryObject<EntityType<UnzippedArmEntity>> PLAYER_ARM = ENTITIES.register("player_arm",
            () -> EntityType.Builder.<UnzippedArmEntity>of(UnzippedArmEntity::new, EntityClassification.MISC).sized(0.6F, 0.6F).noSummon()
                    .build(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "player_arm").toString()));

    public static final RegistryObject<EntityType<UnzippedLegEntity>> PLAYER_LEG = ENTITIES.register("player_leg",
            () -> EntityType.Builder.<UnzippedLegEntity>of(UnzippedLegEntity::new, EntityClassification.MISC).sized(0.6F, 0.6F).noSummon()
                    .build(new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "player_leg").toString()));

    @SubscribeEvent
    public static void applyAttriubtes(EntityAttributeCreationEvent event){
        event.put(InitEntities.PLAYER_HEAD.get(), BodyPartEntity.createBodyPartAttributes().build());
        event.put(InitEntities.PLAYER_ARM.get(), BodyPartEntity.createBodyPartAttributes().build());
        event.put(InitEntities.PLAYER_LEG.get(), BodyPartEntity.createBodyPartAttributes().build());
    }

};
