package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.KineticWeapon;
import vplus.combat.SpearCharge;
import vplus.weapon.Profiles;

@Mixin(KineticWeapon.class)
public abstract class KineticWeaponMixin {
	@Inject(method = "damageEntities", at = @At("HEAD"), cancellable = true)
	private void vplus$formula(ItemStack stack, int remainingUse, LivingEntity user, EquipmentSlot slot, CallbackInfo ci) {
		if (Profiles.of(stack.getItem()) != null) {
			ci.cancel();
			SpearCharge.replace((KineticWeapon) (Object) this, stack, remainingUse, user, slot);
		}
	}
}
