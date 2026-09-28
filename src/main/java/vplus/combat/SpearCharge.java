package vplus.combat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import vplus.weapon.MaterialTier;
import vplus.weapon.Profile;
import vplus.weapon.Profiles;

public final class SpearCharge {
	private SpearCharge() {
	}

	public static void replace(KineticWeapon kinetic, ItemStack stack, int remainingUse, LivingEntity user, EquipmentSlot slot) {
		int used = stack.getUseDuration(user) - remainingUse;
		if (used < kinetic.delayTicks()) {
			return;
		}
		int phase = used - kinetic.delayTicks();
		Profile profile = Profiles.of(stack.getItem());
		if (profile == null || !(user instanceof net.minecraft.world.entity.player.Player player)) {
			return;
		}
		if (!(user.level() instanceof ServerLevel level)) {
			return;
		}
		Vec3 look = user.getLookAngle();
		double attackerSpeed = look.dot(KineticWeapon.getMotion(user));
		AttackRange range = user.entityAttackRange();
		var hits = net.minecraft.world.entity.projectile.ProjectileUtil.getHitEntitiesAlong(user, range, entity -> entity != user && entity.isAttackable(), ClipContext.Block.COLLIDER);
		if (hits.right().isEmpty()) {
			return;
		}
		float stab = user.getOffhandItem().isEmpty() && profile.twoDamage > 0.0f ? profile.twoDamage : profile.oneDamage;
		boolean any = false;
		for (EntityHitResult hit : hits.right().get()) {
			Entity target = hit.getEntity();
			if (target instanceof EnderDragonPart part) {
				target = part.parentMob;
			}
			if (user.wasRecentlyStabbed(target, kinetic.contactCooldownTicks())) {
				continue;
			}
			user.rememberStabbedEntity(target);
			double targetSpeed = look.dot(KineticWeapon.getMotion(target));
			double relative = Math.max(0.0, attackerSpeed - targetSpeed);
			boolean bite = phase < 24;
			boolean exhaust = phase >= 24 && phase < 40;
			boolean dismount = bite && relative >= 6.0;
			boolean knockback = bite || exhaust;
			float extra = 0.0f;
			if (relative >= 4.6) {
				extra = Math.min(cap(profile), Mth.floor(relative * 0.5));
			}
			float damage = stab + extra;
			if (!bite && !exhaust) {
				damage *= 0.5f;
				knockback = false;
			}
			any |= user.stabAttack(slot, target, damage, true, knockback, dismount);
		}
		if (any) {
			stack.hurtAndBreak(1, user, slot);
			level.broadcastEntityEvent(user, (byte) 2);
		}
	}

	private static int cap(Profile profile) {
		return switch (tierGuess(profile)) {
			case WOOD, STONE, COPPER, LEATHER, BAMBOO -> 3;
			case IRON, GOLD -> 4;
			case DIAMOND -> 5;
			case NETHERITE -> 6;
		};
	}

	private static MaterialTier tierGuess(Profile profile) {
		String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(profile.item).getPath();
		if (path.startsWith("netherite")) {
			return MaterialTier.NETHERITE;
		}
		if (path.startsWith("diamond")) {
			return MaterialTier.DIAMOND;
		}
		if (path.startsWith("golden")) {
			return MaterialTier.GOLD;
		}
		if (path.startsWith("wooden") || path.startsWith("bamboo") || path.startsWith("leather")) {
			return MaterialTier.WOOD;
		}
		if (path.startsWith("stone") || path.startsWith("copper")) {
			return MaterialTier.STONE;
		}
		return MaterialTier.IRON;
	}
}
