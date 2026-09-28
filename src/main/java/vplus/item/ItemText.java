package vplus.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import vplus.VPlusMod;

public final class ItemText {
	private static final String[] MATERIAL_PREFIXES = {
			"netherite_", "diamond_", "golden_", "copper_", "wooden_", "leather_", "bamboo_", "stone_", "iron_"
	};

	private ItemText() {
	}

	public static void appendDescription(ItemStack stack, java.util.function.Consumer<Component> lines) {
		var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
		if (id == null || !VPlusMod.MOD_ID.equals(id.getNamespace())) {
			return;
		}
		String key = descriptionKey(id.getPath());
		if (key != null && Language.getInstance().has(key)) {
			lines.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
		}
	}

	private static String descriptionKey(String path) {
		String specific = "item." + VPlusMod.MOD_ID + "." + path + ".desc";
		if (Language.getInstance().has(specific)) {
			return specific;
		}
		String family = path;
		for (String prefix : MATERIAL_PREFIXES) {
			if (path.startsWith(prefix)) {
				family = path.substring(prefix.length());
				break;
			}
		}
		String familyKey = "item." + VPlusMod.MOD_ID + ".family." + family + ".desc";
		return Language.getInstance().has(familyKey) ? familyKey : null;
	}
}
