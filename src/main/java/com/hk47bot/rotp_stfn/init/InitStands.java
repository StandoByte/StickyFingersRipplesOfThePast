package com.hk47bot.rotp_stfn.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityBlock;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.StandEntityLightAttack;
import com.github.standobyte.jojo.action.stand.StandEntityMeleeBarrage;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.action.stand.*;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpStickyFingersAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpStickyFingersAddon.MOD_ID);
    
 // ======================================== Sticky Fingers ========================================
    
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_PUNCH = ACTIONS.register("sticky_fingers_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()));
    
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_BARRAGE = ACTIONS.register("sticky_fingers_barrage",
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()));

    public static final RegistryObject<StandEntityHeavyAttack> STICKY_FINGERS_HEAVY_PUNCH = ACTIONS.register("sticky_fingers_heavy_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder().shiftVariationOf(STICKY_FINGERS_PUNCH).shiftVariationOf(STICKY_FINGERS_BARRAGE)
                    .partsRequired(StandPart.ARMS)));
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_EXTENDED_PUNCH = ACTIONS.register("sticky_fingers_extended_punch",
            () -> new StickyFingersExtendedPunch(new StandEntityAction.Builder().staminaCost(375).standPerformDuration(20).cooldown(20, 60)
                    .ignoresPerformerStun().resolveLevelToUnlock(3)
                    .standOffsetFront().standPose(StandPose.RANGED_ATTACK)
                    .partsRequired(StandPart.ARMS)));
    
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_BLOCK = ACTIONS.register("sticky_fingers_block",
            () -> new StandEntityBlock());



    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_GET_INTO_MOB = ACTIONS.register("sticky_fingers_get_into_mob",
            () -> new StickyFingersGetInsideMob(new StandEntityAction.Builder().partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandAction> STICKY_FINGERS_TEST = ACTIONS.register("test",
            () -> new StickyFingersOpenPlayerStorage(new StandAction.Builder()));

    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_PLACE_ZIPPER = ACTIONS.register("sticky_fingers_place_zipper",
            () -> new StickyFingersPlaceZipper(new StandEntityAction.Builder().holdType()));

    public static final RegistryObject<StandAction> STICKY_FINGERS_TOGGLE_ZIPPER = ACTIONS.register("sticky_fingers_toggle_zipper",
            () -> new StickyFingersToggleZipper(new StandAction.Builder().shiftVariationOf(STICKY_FINGERS_PLACE_ZIPPER)));


    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<StickyFingersEntity>> STAND_STICKY_FINGERS = 
            new EntityStandRegistryObject<>("sticky_fingers", 
                    STANDS, 
                    () -> new EntityStandType<StandStats>(
                            0x3D66D2, ModStandsInit.PART_5_NAME,

                            new StandAction[] {
                                    STICKY_FINGERS_PUNCH.get(), 
                                    STICKY_FINGERS_BARRAGE.get(),
                                    STICKY_FINGERS_EXTENDED_PUNCH.get()
                                    },
                            new StandAction[] {
                                    STICKY_FINGERS_BLOCK.get(),
                                    STICKY_FINGERS_GET_INTO_MOB.get(),
                                    STICKY_FINGERS_PLACE_ZIPPER.get(),
                                    STICKY_FINGERS_TEST.get()
                                    },

                            StandStats.class, new StandStats.Builder()
                            .tier(6)
                            .power(14.0)
                            .speed(14.0)
                            .range(2.0, 2.0)
                            .durability(7.0)
                            .precision(10.0)
                            .build("Sticky Fingers"), 

                            new StandType.StandTypeOptionals()
                            .addSummonShout(InitSounds.STICKY_FINGERS)
                            .addOst(InitSounds.STICKY_FINGERS_OST)), 

                    InitEntities.ENTITIES, 
                    () -> new StandEntityType<StickyFingersEntity>(StickyFingersEntity::new, 0.65F, 1.95F)
                    .summonSound(InitSounds.STICKY_FINGERS_SUMMON)
                    .unsummonSound(InitSounds.STICKY_FINGERS_UNSUMMON))
            .withDefaultStandAttributes();
}
