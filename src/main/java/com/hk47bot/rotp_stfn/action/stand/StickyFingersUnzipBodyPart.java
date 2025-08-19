package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityActionModifier;
import com.github.standobyte.jojo.action.stand.punch.IPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.entity.stand.TargetHitPart;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerArmEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.PlayerLegEntity;
import com.hk47bot.rotp_stfn.init.InitSounds;
import com.hk47bot.rotp_stfn.network.AddonPackets;
import com.hk47bot.rotp_stfn.network.EntityRemoveHeadPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

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
            ZipperWorldCap zipperWorldCap = user.level.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
            if (zipperWorldCap != null && targetEntity instanceof LivingEntity && zipperWorldCap.isHumanoid((LivingEntity) targetEntity) && !(targetEntity instanceof BodyPartEntity)) {
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
                        world.playLocalSound(entity.getX(), entity.getY(0.5), entity.getZ(), InitSounds.STICKY_FINGERS_UNZIP_PART.get(),
                                standEntity.getSoundSource(), 1.0F, 1.0F, false);
                    }
                } else if (triggerEffect) {
                    LivingEntity targetEntity = StandUtil.getStandUser((LivingEntity) entity);
                    EntityZipperCapability capability = targetEntity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).orElse(null);
                    BodyPartEntity partToKnockback = null;
                    switch (hitPart) {
                        case HEAD:
                            if (capability.isHasHead()) {
                                capability.setHead(false);
                                PlayerHeadEntity head = new PlayerHeadEntity(world, targetEntity);
                                Vector3d position = targetEntity.position().add(0, targetEntity.getBbHeight(), 0);
                                head.moveTo(position.x, position.y, position.z, targetEntity.yRot, targetEntity.xRot);
                                world.addFreshEntity(head);
                                partToKnockback = head;
                            }
                            break;
                        case TORSO_ARMS:
                            if (!capability.noArms()){
                                Random random = userPower.getUser().getRandom();
                                boolean isRight = false;
                                if (!capability.isLeftArmBlocked() && (random.nextBoolean() || capability.isRightArmBlocked())) {
                                    capability.setLeftArmBlocked(true);
                                }
                                else if (!capability.isRightArmBlocked() && (random.nextBoolean() || capability.isLeftArmBlocked())) {
                                    capability.setRightArmBlocked(true);
                                    isRight = true;
                                }
                                else {
                                    break;
                                }
                                PlayerArmEntity arm = new PlayerArmEntity(world, targetEntity, isRight);
                                Vector3d position = targetEntity.position();
                                arm.moveTo(position.x, position.y, position.z, targetEntity.yRot, targetEntity.xRot);
                                world.addFreshEntity(arm);
                                partToKnockback = arm;
                            }
                            break;
                        case LEGS:
                            boolean isRight = false;
                            if (!capability.noLegs()) {
                                switch (targetEntity.getMainArm()) {
                                    case RIGHT:
                                        if (capability.isLeftLegBlocked()) {
                                            capability.setRightLegBlocked(true);
                                            isRight = true;
//                                            leg.setRight(true);
                                        } else {
                                            capability.setLeftLegBlocked(true);
                                            isRight = false;
//                                            leg.setRight(false);
                                        }
                                        break;
                                    case LEFT:
                                        if (capability.isRightLegBlocked()) {
                                            capability.setLeftLegBlocked(true);
//                                            leg.setRight(false);
                                            isRight = false;
                                        } else {
                                            capability.setRightLegBlocked(true);
//                                            leg.setRight(true)
                                            isRight = true;
                                        }
                                        break;
                                }
                                PlayerLegEntity leg = new PlayerLegEntity(world, targetEntity, isRight);
                                Vector3d position = targetEntity.position();
                                leg.moveTo(position.x, position.y, position.z, targetEntity.yRot, targetEntity.xRot);
                                world.addFreshEntity(leg);
                                partToKnockback = leg;
                            }
                            break;
                    }
                    IPunch punch = standEntity.getLastPunch();
                    float damageDealt = punch.getType() == ActionTarget.TargetType.ENTITY ? ((StandEntityPunch) punch).getDamageDealtToLiving() : 0;
                    targetEntity.setHealth(targetEntity.getHealth() + damageDealt * 0.5F);
                    if (partToKnockback != null){
                        DamageUtil.knockback(partToKnockback, 1.0F, userPower.getUser().yHeadRot);
                    }
                }
            }
        }
    }
}
