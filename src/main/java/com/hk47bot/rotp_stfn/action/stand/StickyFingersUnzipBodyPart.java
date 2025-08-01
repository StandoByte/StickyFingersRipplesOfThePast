package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.CrazyDiamondHeal;
import com.github.standobyte.jojo.action.stand.StandEntityActionModifier;
import com.github.standobyte.jojo.action.stand.punch.IPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.entity.stand.TargetHitPart;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class StickyFingersUnzipBodyPart extends StandEntityActionModifier {
    private final TargetHitPart partToHit;

    public StickyFingersUnzipBodyPart(Builder builder, TargetHitPart partToHit) {
        super(builder);
        this.partToHit = partToHit;
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (power.isActive()) {
            Entity targetEntity = target.getEntity();
            if (targetEntity instanceof LivingEntity && StandUtil.getStandUser((LivingEntity) targetEntity) instanceof PlayerEntity) {
                StandEntity standEntity = (StandEntity) power.getStandManifestation();
                TargetHitPart hitPart = standEntity.getCurrentTask().map(task -> {
                    if (task.hasModifierAction(null)) {
                        return null;
                    }
                    return task.getAdditionalData().peekOrNull(TargetHitPart.class);
                }).orElse(null);
                return ActionConditionResult.noMessage(hitPart != null && hitPart == this.partToHit);
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void standTickRecovery(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        TargetHitPart hitPart = task.getAdditionalData().peekOrNull(TargetHitPart.class);
        if (hitPart == null) return;

        boolean triggerEffect = task.getTicksLeft() <= 1;
        if (task.getAdditionalData().isEmpty(TriggeredFlag.class) && task.getTarget().getType() == ActionTarget.TargetType.ENTITY) {
            Entity entity = task.getTarget().getEntity();
            if (entity.isAlive() && entity instanceof LivingEntity) {
                if (world.isClientSide()) {
                    if (task.getTick() == 0 && ClientUtil.canHearStands()) {
                        world.playLocalSound(entity.getX(), entity.getY(0.5), entity.getZ(), ModSounds.CRAZY_DIAMOND_FIX_STARTED.get(),
                                standEntity.getSoundSource(), 1.0F, 1.0F, false);
                    }
                }
                else if (triggerEffect) {
                    LivingEntity targetEntity = StandUtil.getStandUser((LivingEntity) entity);


                    EntityZipperCapability capability = targetEntity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
                    switch (hitPart) {
                        case HEAD:
                            targetEntity.addEffect(new EffectInstance(Effects.CONFUSION, 60, 0, false, false, true));
                            targetEntity.addEffect(new EffectInstance(ModStatusEffects.MISSHAPEN_FACE.get(), 200, 0, false, false, true));
                            break;
                        case TORSO_ARMS:
                            if (capability != null){
                                capability.setLeftArmBlocked(true);
                            }
                            break;
                        case LEGS:
                            targetEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 1, false, false, true));
                            targetEntity.addEffect(new EffectInstance(ModStatusEffects.MISSHAPEN_LEGS.get(), 200, 0, false, false, true));
                            break;
                    }

                    IPunch punch = standEntity.getLastPunch();
                    float damageDealt = punch.getType() == ActionTarget.TargetType.ENTITY ? ((StandEntityPunch) punch).getDamageDealtToLiving() : 0;
                    targetEntity.setHealth(targetEntity.getHealth() + damageDealt * 0.5F);
                }
            }
        }
    }
}
