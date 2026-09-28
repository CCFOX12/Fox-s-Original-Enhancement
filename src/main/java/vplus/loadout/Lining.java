package vplus.loadout;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class Lining {
	private Lining() {
	}

	public static float reduce(net.minecraft.world.entity.LivingEntity target, net.minecraft.world.damagesource.DamageSource source, float amount) {
		if (amount < 2.0f || !(target instanceof Player player) || !worn(player)) {
			return amount;
		}
		if (!source.is(net.minecraft.world.damagesource.DamageTypes.PLAYER_ATTACK) && !source.is(net.minecraft.world.damagesource.DamageTypes.SPEAR)) {
			return amount;
		}
		return amount - 1.0f;
	}

	public static boolean worn(net.minecraft.world.entity.LivingEntity target) {
		return target instanceof Player player && vplus.item.ModGear.kind(Loadout.get(player).get(LoadoutSlots.LINING), "gear_lining");
	}
}
