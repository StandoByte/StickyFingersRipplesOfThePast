package com.hk47bot.rotp_stfn.client.render.model.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.github.standobyte.jojo.client.render.entity.pose.IModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPose;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPoseTransition;
import com.github.standobyte.jojo.client.render.entity.pose.ModelPoseTransitionMultiple;
import com.github.standobyte.jojo.client.render.entity.pose.RotationAngle;
import com.github.standobyte.jojo.client.render.entity.pose.anim.PosedActionAnimation;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.hk47bot.rotp_stfn.entity.stand.stands.StickyFingersEntity;

import net.minecraft.client.renderer.model.ModelRenderer;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class StickyFingersModel extends HumanoidStandModel<StickyFingersEntity> {

		ModelRenderer helmet;
		ModelRenderer cube_r1;
		ModelRenderer cube_r2;
		ModelRenderer zipper2;
		ModelRenderer zipper1;
		ModelRenderer zipper3;
		ModelRenderer zipper4;
		ModelRenderer foreArm;
		ModelRenderer shortForeArm;
	    public StickyFingersModel() {
        super();
		addHumanoidBaseBoxes(null);


		texWidth = 128;
		texHeight = 128;

		head.texOffs(27, 4).addBox(-0.5F, -0.75F, -4.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		helmet = new ModelRenderer(this);
		helmet.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(helmet);
		helmet.texOffs(55, 2).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 5.0F, 8.0F, 0.1F, false);
		helmet.texOffs(32, 0).addBox(-1.0F, -8.5F, -4.5F, 2.0F, 6.0F, 9.0F, -0.05F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -7.0F, 4.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -7.0F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -4.75F, 4.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -2.75F, 3.75F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -4.75F, -5.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -9.25F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -9.25F, -3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		helmet.texOffs(2, 4).addBox(-0.5F, -9.25F, 2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, -8.75F, 4.75F);
		helmet.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
		cube_r1.texOffs(2, 4).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, -8.75F, -4.25F);
		helmet.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
		cube_r2.texOffs(2, 4).addBox(-0.5F, -0.25F, -0.25F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		torso.texOffs(0, 64).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		torso.texOffs(25, 63).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 4.0F, 1.0F, -0.2F, false);
		torso.texOffs(29, 68).addBox(-2.0F, 3.5F, -2.25F, 4.0F, 7.0F, 1.0F, -0.1F, false);
		torso.texOffs(0, 80).addBox(-4.0F, 11.0F, -2.0F, 8.0F, 1.0F, 4.0F, 0.1F, false);

		zipper2 = new ModelRenderer(this);
		zipper2.setPos(0.0F, 11.75F, -2.0F);
		torso.addChild(zipper2);
		zipper2.texOffs(0, 37).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		zipper2.texOffs(0, 39).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);
		zipper2.texOffs(0, 43).addBox(-1.0F, 1.375F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1F, false);
		zipper2.texOffs(0, 47).addBox(-1.0F, 2.875F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);

		zipper1 = new ModelRenderer(this);
		zipper1.setPos(0.0F, 0.75F, -2.0F);
		torso.addChild(zipper1);
		setRotationAngle(zipper1, -0.0873F, 0.0F, 0.0F);
		zipper1.texOffs(5, 22).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		zipper1.texOffs(0, 21).addBox(-0.5F, 0.25F, -0.5F, 1.0F, 2.0F, 1.0F, -0.11F, false);
		zipper1.texOffs(0, 25).addBox(-1.0F, 1.125F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1F, false);
		zipper1.texOffs(0, 29).addBox(-1.0F, 2.375F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		leftArm.texOffs(32, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		leftArm.texOffs(32, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.1F, false);

		leftForeArm.texOffs(32, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.001F, false);
		leftForeArm.texOffs(33, 91).addBox(1.5F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F, -0.1F, false);
		leftForeArm.texOffs(40, 92).addBox(1.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.11F, false);
		leftForeArm.texOffs(40, 92).addBox(1.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(40, 92).addBox(1.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		leftForeArm.texOffs(40, 92).addBox(1.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.11F, false);

		zipper3 = new ModelRenderer(this);
		zipper3.setPos(2.5F, 1.0F, 0.0F);
		leftForeArm.addChild(zipper3);
		setRotationAngle(zipper3, 0.0F, -1.5708F, -0.0873F);
		zipper3.texOffs(5, 22).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.08F, false);
		zipper3.texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.21F, false);
		zipper3.texOffs(0, 25).addBox(-1.0F, 1.125F, -0.5F, 2.0F, 2.0F, 1.0F, -0.2F, false);
		zipper3.texOffs(0, 29).addBox(-1.0F, 2.375F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1F, false);

		rightArm.texOffs(0, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		rightArm.texOffs(0, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.1F, false);

		rightForeArm.texOffs(0, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.001F, false);
		rightForeArm.texOffs(0, 91).addBox(-2.5F, 0.0F, -2.0F, 1.0F, 6.0F, 4.0F, -0.1F, false);
		rightForeArm.texOffs(7, 92).addBox(-2.25F, 5.25F, 1.0F, 1.0F, 1.0F, 1.0F, -0.11F, false);
		rightForeArm.texOffs(7, 92).addBox(-2.25F, 5.25F, 0.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(7, 92).addBox(-2.25F, 5.25F, -1.0F, 1.0F, 1.0F, 1.0F, -0.1F, false);
		rightForeArm.texOffs(7, 92).addBox(-2.25F, 5.25F, -2.0F, 1.0F, 1.0F, 1.0F, -0.11F, false);

		zipper4 = new ModelRenderer(this);
		zipper4.setPos(-2.5F, 1.0F, 0.0F);
		rightForeArm.addChild(zipper4);
		setRotationAngle(zipper4, 0.0F, 1.5708F, 0.0873F);
		zipper4.texOffs(5, 22).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, -0.08F, false);
		zipper4.texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, -0.21F, false);
		zipper4.texOffs(0, 25).addBox(-1.0F, 1.125F, -0.5F, 2.0F, 2.0F, 1.0F, -0.2F, false);
		zipper4.texOffs(0, 29).addBox(-1.0F, 2.375F, -0.5F, 2.0F, 2.0F, 1.0F, -0.1F, false);

		leftLegJoint.texOffs(99, 98).addBox(-1.0F, -1.0F, -2.25F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		rightLegJoint.texOffs(67, 98).addBox(-1.0F, -1.0F, -2.25F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		rightLowerLeg.texOffs(64, 118).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, -0.001F, false);

		}

	@Override
	public void prepareMobModel(StickyFingersEntity entity, float walkAnimPos, float walkAnimSpeed, float partialTick) {
		super.prepareMobModel(entity, walkAnimPos, walkAnimSpeed, partialTick);
		if (rightForeArm != null && rightArmJoint != null) {
			rightForeArm.visible = entity.hasForeArm();
			rightArmJoint.visible = entity.hasShortForeArm();
		}
	}

	@Override
	protected RotationAngle[][] initSummonPoseRotations() {
		return new RotationAngle[][] {
				new RotationAngle[] {
						RotationAngle.fromDegrees(head, 0, -15F, 0),
						RotationAngle.fromDegrees(body, -10F, -10F, 0),
						RotationAngle.fromDegrees(upperPart, 0, 0, 0),
						RotationAngle.fromDegrees(leftArm, 10F, 0, -2.5F),
						RotationAngle.fromDegrees(leftForeArm, -15F, 0, 2.5F),
						RotationAngle.fromDegrees(rightArm, 10F, 0, 2.5F),
						RotationAngle.fromDegrees(rightForeArm, -2.5F, 0, 2.5F),
						RotationAngle.fromDegrees(leftLeg, 7.5F, 0, -5F),
						RotationAngle.fromDegrees(leftLowerLeg, 2.5F, 0, 5F),
						RotationAngle.fromDegrees(rightLeg, 5F, 0, 5F),
						RotationAngle.fromDegrees(rightLowerLeg, 2.5F, 0, -5)
				},
				new RotationAngle[] {
						RotationAngle.fromDegrees(head, 0, 0, 0),
						RotationAngle.fromDegrees(body, 5F, -20F, 0),
						RotationAngle.fromDegrees(upperPart, 0, 0, 0),
						RotationAngle.fromDegrees(leftArm, 0, 0, 0),
						RotationAngle.fromDegrees(leftForeArm, -7.5F, 0, 0),
						RotationAngle.fromDegrees(rightArm, 0, -50F, 20F),
						RotationAngle.fromDegrees(rightForeArm, -40F, 0, 0),
						RotationAngle.fromDegrees(leftLeg, -15F, -15F, 0),
						RotationAngle.fromDegrees(leftLowerLeg, 10F, 0, 0),
						RotationAngle.fromDegrees(rightLeg, -7.5F, 15F, 0),
						RotationAngle.fromDegrees(rightLowerLeg, 2.5F, 0, 0)
				}
		};
	}

	@Override
	protected void initActionPoses() {
		actionAnim.put(StandPose.RANGED_ATTACK, new PosedActionAnimation.Builder<StickyFingersEntity>()
				.addPose(StandEntityAction.Phase.BUTTON_HOLD, new ModelPose<>(new RotationAngle[] {
						new RotationAngle(body, 0.0F, -0.48F, 0.0F),
						new RotationAngle(leftArm, 0.0F, 0.0F, 0.0F),
						new RotationAngle(leftForeArm, 0.0F, 0.0F, 0.0F),
						new RotationAngle(rightArm, -1.0908F, 0.0F, 1.5708F),
						new RotationAngle(rightForeArm, 0.0F, 0.0F, 0.0F)
				}))
				.build(idlePose));

		super.initActionPoses();
	}

	@Override
	protected ModelPose<StickyFingersEntity> initIdlePose() {
		return new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(body, -5F, 30F, 0.0F),
				RotationAngle.fromDegrees(upperPart, 0.0F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(torso, 0.0F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(leftArm, 12.5F, -30F, -15F),
				RotationAngle.fromDegrees(leftForeArm, -12.5F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(rightArm, 10F, 30F, 15F),
				RotationAngle.fromDegrees(rightForeArm, -15F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(leftLeg, 20F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(leftLowerLeg, 0.0F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(rightLeg, 0.0F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(rightLowerLeg, 5F, 0.0F, 0.0F)
		});
	}

	@Override
	protected ModelPose<StickyFingersEntity> initIdlePose2Loop() {
		return new ModelPose<>(new RotationAngle[] {
				RotationAngle.fromDegrees(leftArm, 7.5F, -30F, -15F),
				RotationAngle.fromDegrees(leftForeArm, -17.5F, 0.0F, 0.0F),
				RotationAngle.fromDegrees(rightArm, 12.5F, 30F, 15F),
				RotationAngle.fromDegrees(rightForeArm, -17.5F, 0.0F, 0.0F)
		});
	}
}
