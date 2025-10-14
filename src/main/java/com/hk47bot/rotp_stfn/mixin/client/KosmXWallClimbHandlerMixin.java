package com.hk47bot.rotp_stfn.mixin.client;

import com.github.standobyte.jojo.capability.entity.living.LivingWallClimbing;
import com.github.standobyte.jojo.client.playeranim.anim.kosmximpl.hamon.KosmXWallClimbHandler;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = KosmXWallClimbHandler.class, remap = false)
public class KosmXWallClimbHandlerMixin {
    @Redirect(method = "onEntityRender", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/power/impl/nonstand/type/hamon/HamonUtil;emitHamonSparkParticles(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;DDDFF)V"))
    private void changeWallClimbParticles(World world, PlayerEntity clientHandled, double x, double y, double z, float intensity, float volumeMult){
        if (LivingWallClimbing.getHandler(clientHandled).get().isHamon()){
            HamonUtil.emitHamonSparkParticles(world, clientHandled,
                    x, y, z, intensity, volumeMult);
        }
    }

    @Redirect(method = "onRenderFirstPerson", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/power/impl/nonstand/type/hamon/HamonUtil;emitHamonSparkParticles(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;DDDFF)V"))
    private void changeWallClimbParticles2(World world, PlayerEntity clientHandled, double x, double y, double z, float intensity, float volumeMult){
        if (LivingWallClimbing.getHandler(clientHandled).get().isHamon()){
            HamonUtil.emitHamonSparkParticles(world, clientHandled,
                    x, y, z, intensity, volumeMult);
        }
    }
}
