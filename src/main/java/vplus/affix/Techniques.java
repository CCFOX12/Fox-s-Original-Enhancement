package vplus.affix;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vplus.combat.CombatMemory;
import vplus.combat.Grip;
import vplus.item.ModComponents;
import vplus.weapon.Profile;
import vplus.weapon.ShieldSpec;

public final class Techniques {
	public static final String BASH = "bash";
	public static final String BURST = "burst";
	public static final String FAN = "fan";
	public static final String LUNGE = "lunge";
	public static final String STOMP = "stomp";

	private Techniques() {
	}

	public static String name(String id) {
		return switch (id) {
			case BASH -> "冲撞";
			case BURST -> "爆风";
			case FAN -> "扇形斩";
			case LUNGE -> "突刺";
			case STOMP -> "跺地";
			default -> id;
		};
	}

	public static String of(ItemStack stack) {
		return stack.get(ModComponents.TECHNIQUE);
	}

	public static boolean tryUse(Player player, ItemStack stack) {
		String id = of(stack);
		if (id == null || !Cooldown.ready(player, id)) {
			return false;
		}
		Profile profile = Grip.of(player).main;
		if (profile == null) {
			return false;
		}
		float damage = switch (id) {
			case FAN -> profile.oneDamage * 0.60f;
			case LUNGE -> profile.oneDamage;
			case STOMP -> profile.oneDamage * 0.40f;
			default -> 0.0f;
		};
		if (damage <= 0.0f) {
			return false;
		}
		Cooldown.start(player, id, switch (id) {
			case FAN -> 60L;
			case LUNGE -> 50L;
			case STOMP -> 70L;
			default -> 60L;
		});
		var box = player.getBoundingBox().inflate(id.equals(LUNGE) ? 2.0 : 3.0);
		int hits = 0;
		if (player.level() instanceof net.minecraft.server.level.ServerLevel level) {
			for (var living : level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, box, entity -> entity != player && player.hasLineOfSight(entity))) {
				CombatMemory.hurt(player, living, damage, id.equals(STOMP) ? vplus.weapon.DamageKind.BLUNT : profile.damageKind);
				if (id.equals(LUNGE) && ++hits >= 1) {
					break;
				}
			}
		}
		stack.hurtAndBreak(2, player, net.minecraft.world.InteractionHand.MAIN_HAND);
		return true;
	}

	public static float bashCooldown(ShieldSpec shield) {
		if (shield == null) {
			return 1.6f;
		}
		return switch (shield.armor()) {
			case 1 -> 1.2f;
			case 3 -> 2.2f;
			case 4 -> 2.8f;
			default -> 1.6f;
		};
	}
}
