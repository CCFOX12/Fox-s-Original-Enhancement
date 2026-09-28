package vplus.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import vplus.VPlusMod;

public final class ModTabs {
	private ModTabs() {
	}

	public static void register() {
		ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "main"));
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
				.title(Component.translatable("itemGroup.vplus.main"))
				.icon(() -> new net.minecraft.world.item.ItemStack(Items.IRON_SWORD))
				.displayItems((parameters, output) -> {
					for (var item : ModItems.CREATIVE) {
						output.accept(item);
					}
				})
				.build());
	}
}
