package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.BlockZipperStorage;
import com.hk47bot.rotp_stfn.capability.EntityZipperStorage;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCap;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCapProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Optional;
import java.util.UUID;

public class StickyFingersOpenPlayerStorage extends StandAction {
    public StickyFingersOpenPlayerStorage(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide() && user instanceof ServerPlayerEntity){
            ZipperStorageCap zipperStorageCap = world.getCapability(ZipperStorageCapProvider.CAPABILITY).orElse(null);
            switch (target.getType()) {
                case EMPTY:
                    break;
                case ENTITY:
                    EntityZipperStorage storage = zipperStorageCap.findEntityStorage(target.getEntity().getUUID(), (ServerPlayerEntity) user);
                    NetworkHooks.openGui((ServerPlayerEntity) user, storage);
                    break;
                case BLOCK:
                    BlockPos pos = target.getBlockPos();
                    BlockZipperStorage storage2 = zipperStorageCap.findBlockStorage(pos, (ServerPlayerEntity) user);
                    NetworkHooks.openGui((ServerPlayerEntity) user, storage2);
                    break;
            }
        }
    }

}
