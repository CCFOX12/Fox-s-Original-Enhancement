package vplus.loadout;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class StrapLight {
	private StrapLight() {
	}

	public static int level(Player player) {
		int light = LoadoutRules.light(player);
		if (miner(Loadout.get(player).get(LoadoutSlots.UTILITY_HEAD)) || miner(player.getItemBySlot(EquipmentSlot.HEAD))) {
			light = Math.max(light, 15);
		}
		return light;
	}

	private static boolean miner(ItemStack stack) {
		return "miner_hat".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath());
	}
}
