package com.hk47bot.rotp_stfn.util;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.IPlayerPossess;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.block.ZipperFace;
import com.hk47bot.rotp_stfn.capability.EntityZipperData;
import com.hk47bot.rotp_stfn.capability.EntityZipperDataProvider;
import com.hk47bot.rotp_stfn.init.InitStands;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GameplayEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event){
        LivingEntity entity = event.getEntityLiving();
        entity.getCapability(EntityZipperDataProvider.CAPABILITY).ifPresent(EntityZipperData::tick);
//        if (entity instanceof ServerPlayerEntity){
//            ServerPlayerEntity player = (ServerPlayerEntity) entity;
//            if (!event.getEntityLiving().level.isClientSide()){
//                if (IStandPower.getPlayerStandPower(player).getType() == InitStands.STAND_STICKY_FINGERS.getStandType() && player instanceof IPlayerPossess && player.isShiftKeyDown()) {
//                    ((IPlayerPossess)player).jojoPossessEntity(null, true, null);
//                }
//            }
//        }
    }
}
