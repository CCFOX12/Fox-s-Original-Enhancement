package vplus.combat;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import vplus.weapon.DamageKind;

public final class CombatMemory {
	private static final ConcurrentHashMap<UUID, Long> BASH_READY = new ConcurrentHashMap<>();

	private CombatMemory() {
	}

	public static boolean bashReady(Player player) {
		Long at = BASH_READY.get(player.getUUID());
		return at == null || player.level().getGameTime() >= at;
	}

	public static void bashUsed(Player player) {
		BASH_READY.put(player.getUUID(), player.level().getGameTime() + 16L);
	}

	public static void hurt(Player player, Entity target, float amount, DamageKind kind) {
		if (!(target instanceof LivingEntity living) || !(player.level() instanceof ServerLevel level) || amount <= 0.0f) {
			return;
		}
		if (kind != null) {
			HitContext.KIND.set(kind);
		}
		try {
			living.hurtServer(level, player.damageSources().playerAttack(player), amount);
		} finally {
			HitContext.KIND.remove();
		}
	}
}
