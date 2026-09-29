package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import vplus.combat.Grip;
import vplus.combat.HitContext;
import vplus.loadout.Lining;
import vplus.weapon.DamageKind;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

@Mixin(LivingEntity.class)
public abstract class LivingEntityHurtMixin {
	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private float vplus$type(float amount, ServerLevel level, DamageSource source, float ignored) {
		if (HitContext.SKIP_TYPE.get() || amount <= 0.0f) {
			return amount;
		}
		if (!(source.getEntity() instanceof Player player)) {
			return amount;
		}
		if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.SPEAR) && !source.is(DamageTypes.ARROW) && !source.is(DamageTypes.TRIDENT) && !source.is(DamageTypes.THROWN)) {
			return amount;
		}
		LivingEntity self = (LivingEntity) (Object) this;
		DamageKind kind = HitContext.KIND.get();
		if (kind == null) {
			Profile profile = Grip.of(player).main;
			if (profile == null || profile.damageKind == DamageKind.NONE) {
				return amount;
			}
			kind = profile.damageKind;
		}
		HitContext.ATTACKER.set(player);
		vplus.affix.Affix affix = vplus.affix.Affix.of(player.getMainHandItem());
		if (affix == vplus.affix.Affix.UNARMORED && self.getArmorValue() <= 0) {
			amount += 1.0f;
		}
		if (affix == vplus.affix.Affix.ARMORED && self.getArmorValue() > 0) {
			amount += 1.0f;
		}
		boolean armored = self.getArmorValue() > 0;
		return amount * kind.factor(armored, false);
	}

	@Inject(method = "getDamageAfterArmorAbsorb", at = @At("RETURN"), cancellable = true)
	private void vplus$lining(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(Lining.reduce((LivingEntity) (Object) this, source, cir.getReturnValue()));
	}

	@Inject(method = "isBlocking", at = @At("RETURN"), cancellable = true)
	private void vplus$offhandBlock(CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (!(self instanceof Player player) || !cir.getReturnValue() || player.getUsedItemHand() != InteractionHand.OFF_HAND) {
			return;
		}
		Profile main = Profiles.of(player.getMainHandItem().getItem());
		if (main != null && main.suppressesOffhand()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "applyItemBlocking", at = @At("HEAD"))
	private void vplus$angleOn(ServerLevel level, DamageSource source, float amount, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Float> cir) {
		Entity attacker = source.getEntity();
		HitContext.ANGLE_BONUS.set(0.0f);
		if ((Object) this instanceof Player player) {
			HitContext.ANGLE_BONUS.set(vplus.loadout.Pauldron.angleBonus(player));
		}
		if (attacker instanceof Player) {
			HitContext.ANGLE_BONUS.set(HitContext.ANGLE_BONUS.get());
		}
	}

	@Inject(method = "applyItemBlocking", at = @At("RETURN"))
	private void vplus$angleOff(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
		HitContext.ANGLE_BONUS.set(0.0f);
		if (cir.getReturnValue() > 0.0f && (Object) this instanceof Player player) {
			vplus.affix.Burst.tryBurst(player);
		}
	}
}
