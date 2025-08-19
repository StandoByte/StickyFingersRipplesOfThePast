package com.hk47bot.rotp_stfn.client;

import com.github.standobyte.jojo.client.ClientUtil;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCap;
import com.hk47bot.rotp_stfn.capability.ZipperWorldCapProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HumanoidParser {
    public static void updateHumanoidsList(ZipperWorldCap cap){
        Minecraft mc = Minecraft.getInstance();
        for (EntityType type : mc.getEntityRenderDispatcher().renderers.keySet()){
            EntityRenderer renderer = mc.getEntityRenderDispatcher().renderers.get(type);
            if (renderer instanceof LivingRenderer){
                LivingRenderer livingRenderer = (LivingRenderer) renderer;
                EntityModel model = livingRenderer.getModel();
                List<String> all = new ArrayList<>();
                for (Field field : model.getClass().getDeclaredFields()) {
                    if (field.getType().equals(ModelRenderer.class)) {
                        field.setAccessible(true);
                        all.add(field.getName().toLowerCase());
                    }
                }
                if ((all.contains("head")
                        && all.contains("body")
                        && all.contains("leftarm")
                        && all.contains("rightarm")
                        && all.contains("leftleg")
                        && all.contains("rightleg"))
                        || model instanceof BipedModel){
                    cap.humanoidTypes.add(type);
                }
            }
        }
    }

    @Nullable
    public static ModelRenderer getPartByName(String name, EntityModel model) {
        World world = ClientUtil.getClientWorld();
        world.getCapability(ZipperWorldCapProvider.CAPABILITY).orElse(null);
        if (model instanceof BipedModel){
            return tryToFindPartInModelClass(name, model, BipedModel.class);
        }
        else {
            return tryToFindPartInModelClass(name, model, model.getClass());
        }
    }

    private static ModelRenderer tryToFindPartInModelClass(String name, EntityModel model, Class<? extends EntityModel> modelClass){
        for (Field field : modelClass.getDeclaredFields()) {
            if (field.getType().equals(ModelRenderer.class) && field.getName().toLowerCase().equals(name)) {
                field.setAccessible(true);
                try {
                    return (ModelRenderer) field.get(model);
                } catch (IllegalAccessException e) {
                    RotpStickyFingersAddon.getLogger().info("No {} was found in model", name);
                }
            }
        }
        return null;
    }
}
