package vplus.loadout;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class StrapLight {
	private StrapLight() {
	}

	public static int level(Player player) {
		int light = LoadoutRules.light(player);
		ItemStack hat = Loadout.get(player).get(LoadoutSlots.UTILITY_HEAD);
		String path = BuiltInRegistries.ITEM.getKey(hat.getItem()).getPath();
		if ("miner_hat".equals(path)) {
			light = Math.max(light, 15);
		}
		return light;
	}
}
