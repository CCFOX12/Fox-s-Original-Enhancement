package vplus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import vplus.combat.CombatMemory;
import vplus.combat.Grip;
import vplus.combat.HitContext;
import vplus.weapon.DamageKind;
import vplus.weapon.Profile;
import vplus.weapon.ShieldSpec;

@Mixin(Player.class)
public abstract class PlayerAttackMixin {
	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void vplus$bash(Entity target, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		if (player.level().isClientSide() || !player.isBlocking()) {
			return;
		}
		Grip grip = Grip.of(player);
		ShieldSpec shield = grip.raisedShield();
		if (shield == null) {
			return;
		}
		HitContext.SKIP_OFFHAND.set(true);
		if (shield.bash() <= 0.0f || !CombatMemory.bashReady(player)) {
			ci.cancel();
			return;
		}
		float scale = player.getAttackStrengthScale(0.5f);
		if (scale > 0.9f) {
			CombatMemory.bashUsed(player);
			CombatMemory.hurt(player, target, shield.bash(), DamageKind.BLUNT);
			player.resetAttackStrengthTicker();
		}
		ci.cancel();
	}

	@Inject(method = "attack", at = @At("RETURN"))
	private void vplus$dual(Entity target, CallbackInfo ci) {
		boolean skip = HitContext.SKIP_OFFHAND.get();
		HitContext.SKIP_OFFHAND.set(false);
		Player player = (Player) (Object) this;
		if (skip || player.level().isClientSide() || !(target instanceof LivingEntity)) {
			return;
		}
		Grip grip = Grip.of(player);
		if (!skip && grip.main != null && "great_hammer".equals(grip.main.id)) {
			shock(player, target, grip.main, player.getAttackStrengthScale(0.5f));
		}
		if (skip || player.level().isClientSide() || !(target instanceof LivingEntity) || !grip.dual || grip.off == null) {
			return;
		}
		float scale = player.getAttackStrengthScale(0.5f);
		float amount = grip.off.oneDamage * 0.5f * scale;
		if (player.fallDistance > 0.0 && !player.onGround() && !player.isSprinting()) {
			amount *= 1.5f;
		}
		CombatMemory.hurt(player, target, amount, grip.off.damageKind);
	}

	@Inject(method = "isSweepAttack", at = @At("RETURN"), cancellable = true)
	private void vplus$canSweep(boolean fullCharge, boolean crit, boolean sprinting, CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
		Profile profile = Grip.of(player).main;
		if (profile == null || profile.sweep <= 0.0f || !fullCharge || crit || sprinting || !player.onGround()) {
			return;
		}
		cir.setReturnValue(true);
	}

	@Inject(method = "doSweepAttack", at = @At("HEAD"), cancellable = true)
	private void vplus$sweep(Entity target, float damage, net.minecraft.world.damagesource.DamageSource source, float scale, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		Profile profile = Grip.of(player).main;
		if (profile == null || profile.sweep <= 0.0f || !(player.level() instanceof net.minecraft.server.level.ServerLevel level)) {
			return;
		}
		ci.cancel();
		float amount = damage * profile.sweep;
		int hit = 0;
		for (LivingEntity nearby : level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
			if (nearby == player || nearby == target || player.isAlliedTo(nearby) || player.distanceToSqr(nearby) >= 9.0) {
				continue;
			}
			nearby.hurtServer(level, source, amount * scale);
			if (++hit >= 3) {
				break;
			}
		}
	}

	private static void shock(Player player, Entity target, Profile profile, float scale) {
		float amount = profile.oneDamage * scale * 0.30f;
		if (!(player.level() instanceof net.minecraft.server.level.ServerLevel level)) {
			return;
		}
		for (LivingEntity nearby : level.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(1.2))) {
			if (nearby == player || nearby == target) {
				continue;
			}
			nearby.hurtServer(level, player.damageSources().playerAttack(player), amount);
		}
	}
}
