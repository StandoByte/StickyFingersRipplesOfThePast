package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.EntityZipperStorage;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCap;
import com.hk47bot.rotp_stfn.capability.ZipperStorageCapProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class StickyFingersOpenStorageInUser extends StandAction {
    public StickyFingersOpenStorageInUser(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide() && user instanceof ServerPlayerEntity){
            ZipperStorageCap zipperStorageCap = world.getCapability(ZipperStorageCapProvider.CAPABILITY).orElse(null);
            EntityZipperStorage storage = zipperStorageCap.findEntityStorage(user.getUUID(), (ServerPlayerEntity) user);
            NetworkHooks.openGui((ServerPlayerEntity) user, storage);
        }
    }
}
