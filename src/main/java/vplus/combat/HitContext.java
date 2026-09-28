package vplus.combat;

import net.minecraft.world.entity.player.Player;
import vplus.weapon.DamageKind;

public final class HitContext {
	public static final ThreadLocal<DamageKind> KIND = new ThreadLocal<>();
	public static final ThreadLocal<Boolean> SKIP_TYPE = ThreadLocal.withInitial(() -> false);
	public static final ThreadLocal<Boolean> SKIP_OFFHAND = ThreadLocal.withInitial(() -> false);
	public static final ThreadLocal<Player> ATTACKER = new ThreadLocal<>();
	public static final ThreadLocal<Float> ANGLE_BONUS = ThreadLocal.withInitial(() -> 0.0f);

	private HitContext() {
	}
}
