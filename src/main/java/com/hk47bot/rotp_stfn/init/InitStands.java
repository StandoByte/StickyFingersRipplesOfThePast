package com.hk47bot.rotp_stfn.init;

import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.*;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.entity.stand.TargetHitPart;
import com.github.standobyte.jojo.entity.stand.stands.MagiciansRedEntity;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mod.StoryPart;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.action.stand.*;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import static com.github.standobyte.jojo.init.ModEntityTypes.ENTITIES;
import static com.github.standobyte.jojo.init.power.ModCommonRegisters.ACTIONS;
import static com.github.standobyte.jojo.init.power.stand.ModStandsInit.STAND_TYPES;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpStickyFingersAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STAND_TYPES = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpStickyFingersAddon.MOD_ID);
    
 // ======================================== Sticky Fingers ========================================
    
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_PUNCH = ACTIONS.register("sticky_fingers_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()
                    .shout(InitSounds.BRUNO_PUNCH)
                    .punchSound(InitSounds.STICKY_FINGERS_PUNCH_LIGHT)));
    
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_BARRAGE = ACTIONS.register("sticky_fingers_barrage",
            () -> new StandEntityMeleeBarrage(new StandEntityMeleeBarrage.Builder()
                    .standSound(StandEntityAction.Phase.PERFORM, false, InitSounds.BRUNO_BARRAGE)
                    .barrageHitSound(InitSounds.STICKY_FINGERS_BARRAGE)));

    public static final RegistryObject<StandEntityActionModifier> STICKY_FINGERS_UNZIP_HEAD = ACTIONS.register("sticky_fingers_unzip_head",
            () -> new StickyFingersUnzipBodyPart(new StandAction.Builder()
                    .staminaCost(50)
                    .resolveLevelToUnlock(1), TargetHitPart.HEAD));

    public static final RegistryObject<StandEntityActionModifier> STICKY_FINGERS_UNZIP_ARM = ACTIONS.register("sticky_fingers_unzip_arm",
            () -> new StickyFingersUnzipBodyPart(new StandAction.Builder()
                    .staminaCost(50)
                    .resolveLevelToUnlock(1), TargetHitPart.TORSO_ARMS));

    public static final RegistryObject<StandEntityActionModifier> STICKY_FINGERS_UNZIP_LEG = ACTIONS.register("sticky_fingers_unzip_leg",
            () -> new StickyFingersUnzipBodyPart(new StandAction.Builder()
                    .staminaCost(50)
                    .resolveLevelToUnlock(1), TargetHitPart.LEGS));

    public static final RegistryObject<StandEntityHeavyAttack> STICKY_FINGERS_FINISHER_PUNCH = ACTIONS.register("sticky_fingers_unzip_punch",
            () -> new StickyFingersUnzipOpponent(new StandEntityHeavyAttack.Builder()
                    .standPose(StandPose.HEAVY_ATTACK)
                    .punchSound(InitSounds.STICKY_FINGERS_FINISHER)
                    .shout(InitSounds.BRUNO_FINISHER)
                    .attackRecoveryFollowup(STICKY_FINGERS_UNZIP_HEAD)
                    .attackRecoveryFollowup(STICKY_FINGERS_UNZIP_ARM)
                    .attackRecoveryFollowup(STICKY_FINGERS_UNZIP_LEG)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityHeavyAttack> STICKY_FINGERS_HEAVY_PUNCH = ACTIONS.register("sticky_fingers_heavy_punch",
            () -> new StandEntityHeavyAttack(new StandEntityHeavyAttack.Builder()
                    .setFinisherVariation(STICKY_FINGERS_FINISHER_PUNCH)
                    .punchSound(InitSounds.STICKY_FINGERS_PUNCH_HEAVY)
                    .shout(InitSounds.BRUNO_HEAVY_PUNCH)
                    .shiftVariationOf(STICKY_FINGERS_PUNCH)
                    .shiftVariationOf(STICKY_FINGERS_BARRAGE)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_EXTENDED_PUNCH = ACTIONS.register("sticky_fingers_extended_punch",
            () -> new StickyFingersExtendedPunch(new StandEntityAction.Builder()
                    .shout(InitSounds.BRUNO_HEAVY_PUNCH)
                    .standSound(InitSounds.STICKY_FINGERS_EXTENDED_PUNCH)
                    .staminaCost(375)
                    .standPerformDuration(25)
                    .cooldown(20, 60)
                    .ignoresPerformerStun()
                    .resolveLevelToUnlock(3)
                    .standOffsetFront()
                    .standPose(StandPose.RANGED_ATTACK)
                    .partsRequired(StandPart.ARMS)));
    
    public static final RegistryObject<StandEntityAction> STICKY_FINGERS_BLOCK = ACTIONS.register("sticky_fingers_block",
            () -> new StandEntityBlock());

    public static final RegistryObject<StandAction> STICKY_FINGERS_GET_INTO_MOB = ACTIONS.register("sticky_fingers_get_into_mob",
            () -> new StickyFingersGetInsideMob(new StandAction.Builder()
                    .holdToFire(60, false)));

    public static final RegistryObject<StandAction> STICKY_FINGERS_OPEN_STORAGE = ACTIONS.register("sticky_fingers_open_storage",
            () -> new StickyFingersOpenStorageInTarget(new StandAction.Builder()));

    public static final RegistryObject<StandAction> STICKY_FINGERS_OPEN_INNER_STORAGE = ACTIONS.register("sticky_fingers_open_inner_storage",
            () -> new StickyFingersOpenStorageInUser(new StandAction.Builder()
                    .shiftVariationOf(STICKY_FINGERS_OPEN_STORAGE)));

    public static final RegistryObject<StandAction> STICKY_FINGERS_REMOVE_ZIPPER = ACTIONS.register("sticky_fingers_remove_zipper",
            () -> new StickyFingersRemoveZipper(new StandAction.Builder()));

    public static final RegistryObject<StandAction> STICKY_FINGERS_PLACE_ZIPPER = ACTIONS.register("sticky_fingers_place_zipper",
            () -> new StickyFingersPlaceZipper(new StandAction.Builder()
                    .holdType()
                    .standPose(StickyFingersPlaceZipper.STAND_POSE)));

    public static final RegistryObject<StandAction> STICKY_FINGERS_TOGGLE_ZIPPER = ACTIONS.register("sticky_fingers_toggle_zipper",
            () -> new StickyFingersToggleZipper(new StandAction.Builder()
                    .shiftVariationOf(STICKY_FINGERS_PLACE_ZIPPER)
                    .shiftVariationOf(STICKY_FINGERS_REMOVE_ZIPPER)
                    .partsRequired(StandPart.ARMS)));

    public static final RegistryObject<StandAction> STICKY_FINGERS_ZIP_USER_WOUNDS = ACTIONS.register("sticky_fingers_zip_user_wounds",
            () -> new StickyFingersZipUserWounds(new StandAction.Builder()));

    public static final RegistryObject<StandAction> STICKY_FINGERS_ZIP_TARGET_WOUNDS = ACTIONS.register("sticky_fingers_zip_target_wounds",
            () -> new StickyFingersZipTargetWounds(new StandAction.Builder()
                    .shiftVariationOf(STICKY_FINGERS_ZIP_USER_WOUNDS)));

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<StickyFingersEntity>> STAND_STICKY_FINGERS =
            new EntityStandRegistryObject<>("sticky_fingers",
                    STAND_TYPES,
                    () -> new EntityStandType.Builder<>()
                            .color(0x3D66D2)
                            .storyPartName(StoryPart.GOLDEN_WIND.getName())
                            .leftClickHotbar(
                                    STICKY_FINGERS_PUNCH.get(),
                                    STICKY_FINGERS_BARRAGE.get(),
                                    STICKY_FINGERS_EXTENDED_PUNCH.get()
                            )
                            .rightClickHotbar(
                                    STICKY_FINGERS_BLOCK.get(),
                                    STICKY_FINGERS_GET_INTO_MOB.get(),
                                    STICKY_FINGERS_PLACE_ZIPPER.get(),
                                    STICKY_FINGERS_OPEN_STORAGE.get(),
                                    STICKY_FINGERS_ZIP_USER_WOUNDS.get()
                            )
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .power(12.0, 14.0)
                                    .speed(11.0, 14.0)
                                    .range(2.0, 2.0)
                                    .durability(7.0, 7.0)
                                    .precision(8.0, 10.0)
                                    .randomWeight(2)
                            )
                            .addSummonShout(InitSounds.BRUNO_STICKY_FINGERS)
                            .addOst(InitSounds.STICKY_FINGERS_OST)
                            .build(),

                    ENTITIES,
                    () -> new StandEntityType<StickyFingersEntity>(StickyFingersEntity::new, 0.65F, 1.95F)
                            .summonSound(InitSounds.STICKY_FINGERS_SUMMON)
                            .unsummonSound(InitSounds.STICKY_FINGERS_UNSUMMON))
                    .withDefaultStandAttributes();
}
