package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.world.item.component.BlocksAttacks;
import vplus.combat.HitContext;

@Mixin(BlocksAttacks.DamageReduction.class)
public abstract class ShieldAngleMixin {
	@ModifyVariable(method = "resolve", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private double vplus$wider(double angle) {
		float bonus = HitContext.ANGLE_BONUS.get();
		if (bonus <= 0.0f) {
			return angle;
		}
		return angle - Math.toRadians(bonus * 0.5f);
	}
}
