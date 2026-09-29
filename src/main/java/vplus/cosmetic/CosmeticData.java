package vplus.cosmetic;

import net.minecraft.world.item.ItemStack;
import vplus.item.ModComponents;

public final class CosmeticData {
	private CosmeticData() {
	}

	public static String mob(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return "";
		}
		String mob = stack.get(ModComponents.COSMETIC_MOB);
		if (mob != null) {
			return mob;
		}
		if (!isCosmetic(stack)) {
			return "";
		}
		String legacy = stack.get(ModComponents.GEMS);
		return legacy == null ? "" : legacy;
	}

	public static int pieces(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return 0;
		}
		Integer pieces = stack.get(ModComponents.COSMETIC_PIECES);
		if (pieces != null) {
			return pieces;
		}
		if (!isCosmetic(stack)) {
			return 0;
		}
		String legacy = stack.get(ModComponents.AFFIX);
		if (legacy == null) {
			return 1;
		}
		try {
			return Integer.parseInt(legacy);
		} catch (NumberFormatException exception) {
			return 1;
		}
	}

	private static boolean isCosmetic(ItemStack stack) {
		String kind = stack.get(ModComponents.SLOT_KIND);
		return kind != null && (kind.startsWith("display_") || kind.startsWith("utility_"));
	}
}
