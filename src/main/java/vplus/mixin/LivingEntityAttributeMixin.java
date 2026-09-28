package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import vplus.combat.Grip;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin {
	private static final ThreadLocal<Boolean> GUARD = ThreadLocal.withInitial(() -> false);

	@Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
	private void vplus$attributes(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {
		if (GUARD.get()) {
			return;
		}
		if ((Object) this instanceof net.minecraft.world.entity.Mob mob && attribute == Attributes.FOLLOW_RANGE) {
			double factor = vplus.cosmetic.CosmeticFollow.factor(mob);
			if (factor != 1.0) {
				cir.setReturnValue(cir.getReturnValue() * factor);
			}
			return;
		}
		if (!((Object) this instanceof Player player)) {
			return;
		}
		if (attribute != Attributes.ATTACK_DAMAGE && attribute != Attributes.ATTACK_SPEED && attribute != Attributes.ENTITY_INTERACTION_RANGE && attribute != Attributes.MOVEMENT_SPEED) {
			return;
		}
		GUARD.set(true);
		try {
			Grip grip = Grip.of(player);
			double value = cir.getReturnValue();
			if (attribute == Attributes.ATTACK_DAMAGE) {
				cir.setReturnValue(value + grip.attackDamage() - grip.shownDamage());
			} else if (attribute == Attributes.ATTACK_SPEED) {
				cir.setReturnValue(value + grip.attackSpeed() - grip.shownSpeed());
			} else if (attribute == Attributes.ENTITY_INTERACTION_RANGE) {
				cir.setReturnValue(value + grip.reach() - grip.shownReach());
			} else if (attribute == Attributes.MOVEMENT_SPEED && player.onGround() && !player.isPassenger() && !player.isFallFlying() && !player.isInWater()) {
				cir.setReturnValue(value * grip.moveMultiplier());
			}
		} finally {
			GUARD.set(false);
		}
	}
}
