package com.hk47bot.rotp_stfn.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingRenderer.class)
public interface LivingRendererInvoker<T extends LivingEntity> {
	@Invoker("scale") public void invokeScale(T entity, MatrixStack matrixStack, float partialTick);
}
