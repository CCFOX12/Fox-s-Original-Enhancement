package vplus.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vplus.VPlusMod;
import vplus.weapon.Profiles;

public final class ModTabs {
	private ModTabs() {
	}

	public static void register() {
		tab("weapons", 0, Items.IRON_SWORD, "itemGroup.vplus.weapons", ModTabs::weapon);
		tab("display", 1, Items.LEATHER_HELMET, "itemGroup.vplus.display", ModTabs::display);
		tab("utility", 2, Items.LEATHER_CHESTPLATE, "itemGroup.vplus.utility", ModTabs::utility);
		tab("rest", 3, Items.IRON_INGOT, "itemGroup.vplus.rest", item -> !weapon(item) && !display(item) && !utility(item));
	}

	private static void tab(String path, int column, Item icon, String title, java.util.function.Predicate<Item> filter) {
		ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, path));
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, CreativeModeTab.builder(CreativeModeTab.Row.TOP, column)
				.title(Component.translatable(title))
				.icon(() -> new ItemStack(icon))
				.displayItems((parameters, output) -> {
					for (Item item : ModItems.CREATIVE) {
						if (filter.test(item)) {
							output.accept(item);
						}
					}
				})
				.build());
	}

	private static boolean weapon(Item item) {
		return Profiles.of(item) != null;
	}

	private static boolean display(Item item) {
		String kind = new ItemStack(item).get(ModComponents.SLOT_KIND);
		return kind != null && kind.startsWith("display_");
	}

	private static boolean utility(Item item) {
		String kind = new ItemStack(item).get(ModComponents.SLOT_KIND);
		return kind != null && (kind.startsWith("utility_") || "pattern".equals(kind));
	}
}
