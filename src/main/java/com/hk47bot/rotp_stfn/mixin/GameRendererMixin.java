package com.hk47bot.rotp_stfn.mixin;

import com.hk47bot.rotp_stfn.entity.bodypart.PlayerHeadEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyVariable(method = "pick", at = @At(value = "STORE"), ordinal = 0)
    private Entity getHeadOwner(Entity entity){
        if (entity instanceof PlayerHeadEntity){
            return ((PlayerHeadEntity) entity).getOwner();
        }
        return entity;
    }
}
