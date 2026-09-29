package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Items;
import vplus.client.cosmetic.SkinComposite;
import vplus.cosmetic.StandLoadout;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidEquipmentMixin {
	@Inject(method = "extractHumanoidRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FLnet/minecraft/client/renderer/item/ItemModelResolver;)V", at = @At("TAIL"))
	private static void vplus$gear(LivingEntity entity, HumanoidRenderState state, float partialTick, ItemModelResolver resolver, CallbackInfo ci) {
		state.setData(SkinComposite.ENTITY, entity.getId());
		state.setData(SkinComposite.GEAR, new SkinComposite.Gear(
				entity.getItemBySlot(EquipmentSlot.HEAD).copy(),
				entity.getItemBySlot(EquipmentSlot.CHEST).copy(),
				entity.getItemBySlot(EquipmentSlot.LEGS).copy(),
				entity.getItemBySlot(EquipmentSlot.FEET).copy()));
		if (entity instanceof ArmorStand stand) {
			state.setData(SkinComposite.STAND, SkinComposite.fromLoadout(StandLoadout.get(stand)));
		}
		if (SkinComposite.hides(entity, EquipmentSlot.HEAD)) {
			state.headEquipment = net.minecraft.world.item.ItemStack.EMPTY;
		}
		if (SkinComposite.hides(entity, EquipmentSlot.CHEST) && !state.chestEquipment.is(Items.ELYTRA)) {
			state.chestEquipment = net.minecraft.world.item.ItemStack.EMPTY;
		}
		if (SkinComposite.hides(entity, EquipmentSlot.LEGS)) {
			state.legsEquipment = net.minecraft.world.item.ItemStack.EMPTY;
		}
		if (SkinComposite.hides(entity, EquipmentSlot.FEET)) {
			state.feetEquipment = net.minecraft.world.item.ItemStack.EMPTY;
		}
	}
}
