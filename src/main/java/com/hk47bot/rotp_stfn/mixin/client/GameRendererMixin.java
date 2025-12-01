package com.hk47bot.rotp_stfn.mixin.client;

import com.hk47bot.rotp_stfn.entity.bodypart.UnzippedHeadEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyVariable(method = "pick", at = @At(value = "STORE"), ordinal = 0)
    private Entity getHeadOwner(Entity entity){
        if (entity instanceof UnzippedHeadEntity){
            return ((UnzippedHeadEntity) entity).getOwner();
        }
        return entity;
    }

}
