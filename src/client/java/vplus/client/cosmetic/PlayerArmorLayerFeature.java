package vplus.client.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PlayerArmorLayerFeature extends RenderLayer<AvatarRenderState, PlayerModel> {
	private static final int HEAD = 1;
	private static final int BODY = 2;
	private static final int RIGHT_ARM = 4;
	private static final int LEFT_ARM = 8;
	private static final int RIGHT_LEG = 16;
	private static final int LEFT_LEG = 32;

	private final Shell head;
	private final Shell chestMain;
	private final Shell chestLeft;
	private final Shell legsMain;
	private final Shell legsLeft;
	private final Shell feetMain;
	private final Shell feetLeft;

	public PlayerArmorLayerFeature(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
		super(parent);
		ArmorModelSet<MeshDefinition> armor = PlayerModel.createArmorMeshSet(new CubeDeformation(0.5F), new CubeDeformation(1.0F));
		this.head = bake(armor.head(), HEAD);
		this.chestMain = bake(armor.chest(), BODY | RIGHT_ARM);
		this.chestLeft = bake(armor.chest(), LEFT_ARM);
		this.legsMain = bake(armor.legs(), BODY | RIGHT_LEG);
		this.legsLeft = bake(armor.legs(), LEFT_LEG);
		this.feetMain = bake(armor.feet(), RIGHT_LEG);
		this.feetLeft = bake(armor.feet(), LEFT_LEG);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, AvatarRenderState state, float limbSwing, float limbSwingAmount) {
		if (state.isInvisible || state.isSpectator) {
			return;
		}
		SkinComposite.ArmorDecal decal = SkinComposite.armorDecal(state);
		if (decal == null) {
			return;
		}
		var pieces = state.getData(SkinComposite.PIECES);
		if (SkinComposite.worn(pieces, EquipmentSlot.HEAD) && renders(state.headEquipment, EquipmentSlot.HEAD)) {
			draw(collector, poseStack, light, state, this.head, decal.main());
		}
		if (SkinComposite.worn(pieces, EquipmentSlot.CHEST) && renders(state.chestEquipment, EquipmentSlot.CHEST) && !state.chestEquipment.is(Items.ELYTRA)) {
			draw(collector, poseStack, light, state, this.chestMain, decal.main());
			draw(collector, poseStack, light, state, this.chestLeft, decal.left());
		}
		if (SkinComposite.worn(pieces, EquipmentSlot.LEGS) && renders(state.legsEquipment, EquipmentSlot.LEGS)) {
			draw(collector, poseStack, light, state, this.legsMain, decal.main());
			draw(collector, poseStack, light, state, this.legsLeft, decal.left());
		}
		if (SkinComposite.worn(pieces, EquipmentSlot.FEET) && renders(state.feetEquipment, EquipmentSlot.FEET)) {
			draw(collector, poseStack, light, state, this.feetMain, decal.main());
			draw(collector, poseStack, light, state, this.feetLeft, decal.left());
		}
	}

	private static boolean renders(ItemStack stack, EquipmentSlot slot) {
		return stack != null && HumanoidArmorLayer.shouldRender(stack, slot);
	}

	private static void draw(SubmitNodeCollector collector, PoseStack poseStack, int light, AvatarRenderState state, Shell shell, Identifier texture) {
		collector.order(16).submitModel(shell, state, poseStack, RenderTypes.createArmorDecalCutoutNoCull(texture), light, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
	}

	private static Shell bake(MeshDefinition mesh, int mask) {
		var root = mesh.getRoot();
		var head = root.getChild("head");
		if (head != null && head.getChild("hat") == null) {
			head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		}
		ModelPart baked = LayerDefinition.create(mesh, 64, 32).bakeRoot();
		return new Shell(baked, mask);
	}

	private static final class Shell extends PlayerModel {
		private final int mask;

		private Shell(ModelPart root, int mask) {
			super(root, false);
			this.mask = mask;
		}

		@Override
		public void setupAnim(AvatarRenderState state) {
			super.setupAnim(state);
			this.head.visible = (this.mask & HEAD) != 0;
			this.hat.visible = false;
			this.body.visible = (this.mask & BODY) != 0;
			this.jacket.visible = false;
			this.rightArm.visible = (this.mask & RIGHT_ARM) != 0;
			this.rightSleeve.visible = false;
			this.leftArm.visible = (this.mask & LEFT_ARM) != 0;
			this.leftSleeve.visible = false;
			this.rightLeg.visible = (this.mask & RIGHT_LEG) != 0;
			this.rightPants.visible = false;
			this.leftLeg.visible = (this.mask & LEFT_LEG) != 0;
			this.leftPants.visible = false;
		}
	}
}
