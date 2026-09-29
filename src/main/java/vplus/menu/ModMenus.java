package vplus.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import vplus.VPlusMod;

public final class ModMenus {
	public static MenuType<StandMenu> STAND;

	private ModMenus() {
	}

	public static void register() {
		STAND = Registry.register(
				BuiltInRegistries.MENU,
				Identifier.fromNamespaceAndPath(VPlusMod.MOD_ID, "stand"),
				new ExtendedScreenHandlerType<>(StandMenu::fromNetwork, StandData.CODEC));
	}
}
