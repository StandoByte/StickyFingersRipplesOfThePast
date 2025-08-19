package com.hk47bot.rotp_stfn.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.capability.BlockZipperStorage;
import com.hk47bot.rotp_stfn.capability.EntityZipperStorage;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.init.InitSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import static com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2.INITIAL_FACING;

public class StickyFingersOpenStorageInTarget extends StandAction {
    public StickyFingersOpenStorageInTarget(StandAction.Builder builder) {
        super(builder);
    }

    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.ANY;
    }
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide() && user instanceof ServerPlayerEntity){
            ZipperWorldCap zipperWorldCap = world.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
            switch (target.getType()) {
                case ENTITY:
                    if ((target.getEntity() instanceof BodyPartEntity)){
                        break;
                    }
                    EntityZipperStorage storage = zipperWorldCap.findEntityStorage(target.getEntity().getUUID(), (ServerPlayerEntity) user);
                    NetworkHooks.openGui((ServerPlayerEntity) user, storage);
                    break;
                case BLOCK:
                    BlockPos pos = target.getBlockPos();
                    RotpStickyFingersAddon.getLogger().info(world.getBlockState(pos));
                    if (world.getBlockState(pos).getBlock() instanceof StickyFingersZipperBlock2){
                        pos = pos.relative(world.getBlockState(pos).getValue(INITIAL_FACING).getOpposite());
                    }
                    BlockZipperStorage storage2 = zipperWorldCap.findBlockStorage(pos, (ServerPlayerEntity) user);
                    NetworkHooks.openGui((ServerPlayerEntity) user, storage2);
                    break;
                default:
                    break;
            }
        }
        else if (ClientUtil.canHearStands()){
            world.playLocalSound(user.getX(), user.getY(), user.getZ(), InitSounds.ZIPPER_OPEN.get(),
                    SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }

}
