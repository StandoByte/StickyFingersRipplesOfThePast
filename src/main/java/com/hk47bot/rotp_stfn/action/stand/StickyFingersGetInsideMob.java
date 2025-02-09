package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.IPlayerPossess;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RotpStickyFingersAddon.MOD_ID)

public class StickyFingersGetInsideMob extends StandEntityAction {
    public StickyFingersGetInsideMob(StandEntityAction.Builder builder) {
        super(builder);
    }
    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.ENTITY;
    }
    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity user = userPower.getUser();
        ActionTarget target = task.getTarget();
        if (!world.isClientSide() && user instanceof IPlayerPossess) {
            Entity targetEntity = target.getEntity();
            ((IPlayerPossess)user).jojoPossessEntity(targetEntity, true, this);
        }
    }
}


