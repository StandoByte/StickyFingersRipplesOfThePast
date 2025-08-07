package com.hk47bot.rotp_stfn.mixin.client;

import com.github.standobyte.jojo.util.general.MathUtil;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapability;
import com.hk47bot.rotp_stfn.capability.EntityZipperCapabilityProvider;
import com.hk47bot.rotp_stfn.entity.bodypart.BodyPartEntity;
import com.hk47bot.rotp_stfn.util.LayerInfo;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.*;

@Mixin(BipedModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> {
    @Shadow public ModelRenderer head;
    @Shadow public ModelRenderer hat;
    @Shadow public ModelRenderer body;
    @Shadow public ModelRenderer rightArm;
    @Shadow public ModelRenderer leftArm;
    @Shadow public ModelRenderer rightLeg;
    @Shadow public ModelRenderer leftLeg;

    @Unique private boolean rotp_stfn_layersSearched = false;
    @Unique private final Map<ModelRenderer, List<LayerInfo>> rotp_stfn_layerMap = new HashMap<>();

    @Inject(method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void rotp_stfn_universalVisibility(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        BipedModel<T> model = (BipedModel<T>) (Object) this;

        if (!rotp_stfn_layersSearched) {
            findLayerParts(model);
            rotp_stfn_layersSearched = true;
        }

        Optional<EntityZipperCapability> capabilityOpt = entity.getCapability(EntityZipperCapabilityProvider.CAPABILITY).resolve();
        if (!capabilityOpt.isPresent()) return;
        EntityZipperCapability capability = capabilityOpt.get();

        setPartAndLayersVisibility(this.head, capability.isHasHead());
        setPartAndLayersVisibility(this.hat, capability.isHasHead());
        setPartAndLayersVisibility(this.leftArm, !capability.isLeftArmBlocked());
        setPartAndLayersVisibility(this.rightArm, !capability.isRightArmBlocked());
        setPartAndLayersVisibility(this.leftLeg, !capability.isLeftLegBlocked());
        setPartAndLayersVisibility(this.rightLeg, !capability.isRightLegBlocked());

        for (Entity passenger : entity.getPassengers()) {
            if (BodyPartEntity.isCarriedTurtle(passenger, entity)) {
                switch (entity.getMainArm()) {
                    case LEFT:
                        rightArm.xRot = -(float)Math.PI / 10f;
                        rightArm.zRot = 0;
                        break;
                    case RIGHT:
                        leftArm.xRot = -(float)Math.PI / 10f;
                        leftArm.zRot = 0;
                        break;
                }
                break;
            }
        }
        if (entity.getPose() == Pose.SWIMMING){
            swimOffset(0, 22);
            head.xRot = (headPitch + 45F) * MathUtil.DEG_TO_RAD;
            body.xRot = 90 * MathUtil.DEG_TO_RAD;
        }
    }

    @Unique
    private void swimOffset(float x, float y){
        head.setPos(head.x + x, y, head.z);
        body.setPos(body.x + x, y, body.z);
        leftArm.setPos(leftArm.x + x, y, leftArm.z);
        rightArm.setPos(rightArm.x + x, y, rightArm.z);
        leftLeg.setPos(leftLeg.x + x, y, leftLeg.z);
        rightLeg.setPos(rightLeg.x + x, y, rightLeg.z);
    }

    @Unique
    private void setPartAndLayersVisibility(ModelRenderer part, boolean visible) {
        if (part != null) {
            part.visible = visible;
            part.children.forEach(child -> child.visible = visible);
            rotp_stfn_layerMap.forEach((base, list) -> {
                if (base.equals(part)) {
                    for (LayerInfo info : list) {
                        info.layer.visible = visible;
//                        RotpStickyFingersAddon.getLogger().info("Layer field '{}' visibility = {}", info.fieldName, visible);
                    }
                }
            });
        }
    }

    @Unique
    private void findLayerParts(BipedModel<T> model) {
        List<ModelRenderer> all = new ArrayList<>();
        for (Field field : model.getClass().getDeclaredFields()) {
            if (field.getType().equals(ModelRenderer.class)) {
                try {
                    field.setAccessible(true);
                    all.add((ModelRenderer) field.get(model));
                } catch (IllegalAccessException ignored) {}
            }
        }

        List<ModelRenderer> base = Arrays.asList(head, hat, body, rightArm, leftArm, rightLeg, leftLeg);
        all.removeAll(base);
        all.removeAll(Collections.singleton(null));

        linkLayer(this.leftArm, all, "leftSleeve", "arm", "sleeve");
        linkLayer(this.rightArm, all, "rightSleeve", "arm", "sleeve");
        linkLayer(this.leftLeg, all, "leftPants", "leg", "pant");
        linkLayer(this.rightLeg, all, "rightPants", "leg", "pant");
        linkLayer(this.body, all, "body", "jacket", "body");
    }

    @Unique
    private void linkLayer(ModelRenderer basePart, List<ModelRenderer> potential, String... keywords) {
        if (basePart == null) return;
        List<LayerInfo> found = new ArrayList<>();
        for (ModelRenderer layer : potential) {
            Field layerField = getFieldFromInstance(layer);
            if (layerField == null) continue;
            String name = layerField.getName().toLowerCase();
            for (String kw : keywords) {
                if (name.contains(kw.toLowerCase())) {
                    found.add(new LayerInfo(layer, layerField.getName()));
                    break;
                }
            }
        }
        if (!found.isEmpty()) {
            rotp_stfn_layerMap.put(basePart, found);
        }
    }

    @Unique
    private Field getFieldFromInstance(ModelRenderer instance) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().equals(ModelRenderer.class)) {
                try {
                    field.setAccessible(true);
                    if (field.get(this) == instance) {
                        return field;
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }
        return null;
    }
}