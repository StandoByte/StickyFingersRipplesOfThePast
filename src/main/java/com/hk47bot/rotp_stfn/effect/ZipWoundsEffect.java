package com.hk47bot.rotp_stfn.effect;

import com.github.standobyte.jojo.init.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ZipWoundsEffect extends Effect {
    public ZipWoundsEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getEffect(ModStatusEffects.BLEEDING.get()) != null){
            entity.removeEffect(ModStatusEffects.BLEEDING.get());
        }
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(1F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int k = 50 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        }
        else {
            return true;
        }
    }


}
