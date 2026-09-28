package vplus.loadout;

import net.minecraft.world.entity.player.Player;
import vplus.item.ModGear;

public final class Pauldron {
	private Pauldron() {
	}

	public static float angleBonus(Player player) {
		return worn(player) ? 10.0f : 0.0f;
	}

	public static float breakReduction(Player player) {
		return worn(player) ? 0.5f : 0.0f;
	}

	private static boolean worn(Player player) {
		return player != null && ModGear.kind(Loadout.get(player).get(LoadoutSlots.PAULDRON), "gear_pauldron");
	}
}
