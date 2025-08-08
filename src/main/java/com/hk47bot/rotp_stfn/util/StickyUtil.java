package com.hk47bot.rotp_stfn.util;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.mrpresident.CocoJumboTurtleEntity;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

import static com.github.standobyte.jojo.util.mc.MCUtil.itemHandFree;

public class StickyUtil {
    public static boolean isHandFree(LivingEntity entity, Hand hand) {
        return areHandsFree(entity, hand);
    }

    public static boolean areBothHandsFree(LivingEntity entity) {
        return areHandsFree(entity, Hand.MAIN_HAND, Hand.OFF_HAND);
    }

    public static boolean areHandsFree(LivingEntity entity, Hand... hands) {
        if (entity.level.isClientSide() && entity.is(ClientUtil.getClientPlayer()) && ClientUtil.arePlayerHandsBusy()) {
            return false;
        }
        for (Hand hand : hands) {
            if (!itemHandFree(entity.getItemInHand(hand))
                    || hand == Hand.OFF_HAND && entity.getPassengers().stream().anyMatch(passenger -> CocoJumboTurtleEntity.isCarriedTurtle(passenger, entity) || BodyPartEntity.isCarriedTurtle(passenger, entity))) {
                return false;
            }
        }
        return true;
    }

    public static @Nullable Entity getEntityByUUID(World world, UUID uuid) {
        if (uuid == null) {
            return null;
        }

        if (world instanceof ClientWorld) {
            ClientWorld controller = (ClientWorld) world;
            for (Entity entity : controller.entitiesForRendering()) {
                if (entity.getUUID().equals(uuid) || entity.getUUID() == uuid) {
                    return entity;
                }
            }
        } else if (world instanceof ServerWorld) {
            ServerWorld controller = (ServerWorld) world;
            return controller.getEntity(uuid);
        }

        return null;
    }
}
