package vplus.client.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

public final class MobSkinFeatureRenderer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {
	private final StandModel model;

	public MobSkinFeatureRenderer(RenderLayerParent<S, M> parent) {
		super(parent);
		ModelPart root = LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.25F), false), 64, 64).bakeRoot();
		this.model = new StandModel(root);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, S state, float limbSwing, float limbSwingAmount) {
		if (state.isInvisible) {
			return;
		}
		Identifier texture = SkinComposite.standTexture(state);
		if (texture == null) {
			return;
		}
		renderColoredCutoutModel(this.model, texture, poseStack, collector, light, state, -1, 1);
	}

	private static final class StandModel extends HumanoidModel<HumanoidRenderState> {
		private final ModelPart jacket;
		private final ModelPart leftSleeve;
		private final ModelPart rightSleeve;
		private final ModelPart leftPants;
		private final ModelPart rightPants;

		private StandModel(ModelPart root) {
			super(root);
			this.jacket = this.body.getChild("jacket");
			this.leftSleeve = this.leftArm.getChild("left_sleeve");
			this.rightSleeve = this.rightArm.getChild("right_sleeve");
			this.leftPants = this.leftLeg.getChild("left_pants");
			this.rightPants = this.rightLeg.getChild("right_pants");
		}

		@Override
		public void setupAnim(HumanoidRenderState state) {
			super.setupAnim(state);
			if (state instanceof ArmorStandRenderState stand) {
				pose(this.head, stand.headPose);
				pose(this.body, stand.bodyPose);
				pose(this.leftArm, stand.leftArmPose);
				pose(this.rightArm, stand.rightArmPose);
				pose(this.leftLeg, stand.leftLegPose);
				pose(this.rightLeg, stand.rightLegPose);
			}
			SkinComposite.StandParts parts = SkinComposite.standParts(state);
			this.head.visible = parts.head();
			this.hat.visible = parts.head();
			this.body.visible = parts.coat();
			this.jacket.visible = parts.coat();
			this.rightArm.visible = parts.coat();
			this.leftArm.visible = parts.coat();
			this.rightSleeve.visible = parts.coat();
			this.leftSleeve.visible = parts.coat();
			this.rightLeg.visible = parts.legs();
			this.leftLeg.visible = parts.legs();
			this.rightPants.visible = parts.legs();
			this.leftPants.visible = parts.legs();
		}

		private static void pose(ModelPart part, net.minecraft.core.Rotations rotations) {
			part.xRot = rotations.x() * ((float) Math.PI / 180.0F);
			part.yRot = rotations.y() * ((float) Math.PI / 180.0F);
			part.zRot = rotations.z() * ((float) Math.PI / 180.0F);
		}
	}
}
