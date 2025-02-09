package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.util.ZipperUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(PlayerEntity.class)
public abstract class NoClipMixin extends Entity {

    @Shadow public abstract <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing);

    @Shadow public abstract boolean isSpectator();
    public NoClipMixin(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method="tick", at = @At(value = "TAIL"/*, target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"*/))
    public void tick(CallbackInfo ci){
//        if (!this.isSpectator()){
//            ZipperUtil.apllyZipperEffects(this);
//        }
    }
}
